package com.example.crowdmonitoring.service;

import com.example.crowdmonitoring.dto.OccupancyUpdateRequest;
import com.example.crowdmonitoring.dto.ZoneCreateRequest;
import com.example.crowdmonitoring.dto.ZoneResponse;
import com.example.crowdmonitoring.model.Zone;
import com.example.crowdmonitoring.repository.ZoneRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class ZoneService {

    private final ZoneRepository zoneRepository;
    private final AlertService alertService;

    public List<ZoneResponse> getAllZones() {
        return zoneRepository.findAll()
                .stream()
                .map(ZoneResponse::fromZone)
                .collect(Collectors.toList());
    }

    public Optional<ZoneResponse> getZoneById(Long id) {
        return zoneRepository.findById(id)
                .map(ZoneResponse::fromZone);
    }

    public ZoneResponse createZone(ZoneCreateRequest request) {
        Zone zone = new Zone();
        zone.setName(request.getName());
        zone.setCapacity(request.getCapacity());
        zone.setCurrentOccupancy(0);
        zone.setDescription(request.getDescription());
        zone.setLocationCoordinates(request.getLocationCoordinates());
        zone.setEmergencyContact(request.getEmergencyContact());

        Zone savedZone = zoneRepository.save(zone);
        log.info("Created new zone: {} with capacity: {}", savedZone.getName(), savedZone.getCapacity());

        return ZoneResponse.fromZone(savedZone);
    }

    public Optional<ZoneResponse> updateOccupancy(Long zoneId, OccupancyUpdateRequest request) {
        Optional<Zone> optionalZone = zoneRepository.findById(zoneId);

        if (optionalZone.isEmpty()) {
            log.warn("Zone not found with id: {}", zoneId);
            return Optional.empty();
        }

        Zone zone = optionalZone.get();
        Zone.ZoneStatus previousStatus = zone.getStatus();

        zone.setCurrentOccupancy(request.getOccupancy());
        zone.updateStatus(); // This updates densityPercentage and status

        Zone savedZone = zoneRepository.save(zone);

        // Check if we need to create an alert
        checkAndCreateAlert(savedZone, previousStatus, request.getSource());

        log.info("Updated occupancy for zone '{}': {} people ({}%)", 
                savedZone.getName(), savedZone.getCurrentOccupancy(), 
                String.format("%.1f", savedZone.getDensityPercentage()));

        return Optional.of(ZoneResponse.fromZone(savedZone));
    }

    private void checkAndCreateAlert(Zone zone, Zone.ZoneStatus previousStatus, String source) {
        Zone.ZoneStatus currentStatus = zone.getStatus();

        // Only create alert if status changed to a more dangerous level
        if (shouldCreateAlert(previousStatus, currentStatus)) {
            String message = generateAlertMessage(zone, source);
            alertService.createOccupancyAlert(zone, message);
        }
    }

    private boolean shouldCreateAlert(Zone.ZoneStatus previous, Zone.ZoneStatus current) {
        if (previous == current) return false;

        return current == Zone.ZoneStatus.WARNING ||
               current == Zone.ZoneStatus.CRITICAL ||
               current == Zone.ZoneStatus.EMERGENCY;
    }

    private String generateAlertMessage(Zone zone, String source) {
        return String.format("Zone '%s' occupancy alert: %d/%d people (%.1f%%) - Source: %s",
                zone.getName(),
                zone.getCurrentOccupancy(),
                zone.getCapacity(),
                zone.getDensityPercentage(),
                source != null ? source : "Unknown");
    }

    public List<ZoneResponse> getDangerousZones() {
        return zoneRepository.findDangerousZones()
                .stream()
                .map(ZoneResponse::fromZone)
                .collect(Collectors.toList());
    }

    public List<ZoneResponse> getZonesByStatus(Zone.ZoneStatus status) {
        return zoneRepository.findByStatus(status)
                .stream()
                .map(ZoneResponse::fromZone)
                .collect(Collectors.toList());
    }

    public boolean deleteZone(Long zoneId) {
        if (zoneRepository.existsById(zoneId)) {
            zoneRepository.deleteById(zoneId);
            log.info("Deleted zone with id: {}", zoneId);
            return true;
        }
        return false;
    }

    public long getEmergencyZoneCount() {
        return zoneRepository.countEmergencyZones();
    }
}