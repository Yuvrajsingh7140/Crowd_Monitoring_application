package com.example.crowdmonitoring.controller;

import com.example.crowdmonitoring.dto.OccupancyUpdateRequest;
import com.example.crowdmonitoring.dto.ZoneCreateRequest;
import com.example.crowdmonitoring.dto.ZoneResponse;
import com.example.crowdmonitoring.model.Zone;
import com.example.crowdmonitoring.service.ZoneService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/zones")
@RequiredArgsConstructor
@CrossOrigin
@Slf4j
@Validated
public class ZoneController {

    private final ZoneService zoneService;

    @GetMapping
    public ResponseEntity<List<ZoneResponse>> getAllZones() {
        List<ZoneResponse> zones = zoneService.getAllZones();
        return ResponseEntity.ok(zones);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ZoneResponse> getZoneById(@PathVariable Long id) {
        Optional<ZoneResponse> zone = zoneService.getZoneById(id);
        return zone.map(ResponseEntity::ok)
                   .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<ZoneResponse> createZone(@Valid @RequestBody ZoneCreateRequest request) {
        try {
            ZoneResponse createdZone = zoneService.createZone(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdZone);
        } catch (Exception e) {
            log.error("Error creating zone: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/{id}/occupancy")
    public ResponseEntity<ZoneResponse> updateOccupancy(
            @PathVariable Long id, 
            @Valid @RequestBody OccupancyUpdateRequest request) {

        Optional<ZoneResponse> updatedZone = zoneService.updateOccupancy(id, request);
        return updatedZone.map(ResponseEntity::ok)
                          .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/dangerous")
    public ResponseEntity<List<ZoneResponse>> getDangerousZones() {
        List<ZoneResponse> dangerousZones = zoneService.getDangerousZones();
        return ResponseEntity.ok(dangerousZones);
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<ZoneResponse>> getZonesByStatus(@PathVariable String status) {
        try {
            Zone.ZoneStatus zoneStatus = Zone.ZoneStatus.valueOf(status.toUpperCase());
            List<ZoneResponse> zones = zoneService.getZonesByStatus(zoneStatus);
            return ResponseEntity.ok(zones);
        } catch (IllegalArgumentException e) {
            log.error("Invalid zone status: {}", status);
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteZone(@PathVariable Long id) {
        boolean deleted = zoneService.deleteZone(id);
        return deleted ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
    }

    @GetMapping("/stats/emergency-count")
    public ResponseEntity<Long> getEmergencyZoneCount() {
        long count = zoneService.getEmergencyZoneCount();
        return ResponseEntity.ok(count);
    }
}