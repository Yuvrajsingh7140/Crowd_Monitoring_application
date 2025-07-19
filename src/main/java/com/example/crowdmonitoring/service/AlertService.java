package com.example.crowdmonitoring.service;

import com.example.crowdmonitoring.dto.AlertResponse;
import com.example.crowdmonitoring.model.Alert;
import com.example.crowdmonitoring.model.Zone;
import com.example.crowdmonitoring.repository.AlertRepository;
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
public class AlertService {

    private final AlertRepository alertRepository;

    public List<AlertResponse> getAllActiveAlerts() {
        return alertRepository.findByIsResolvedFalseOrderByTimestampDesc()
                .stream()
                .map(AlertResponse::fromAlert)
                .collect(Collectors.toList());
    }

    public List<AlertResponse> getAlertsByZone(Zone zone) {
        return alertRepository.findByZoneAndIsResolvedFalseOrderByTimestampDesc(zone)
                .stream()
                .map(AlertResponse::fromAlert)
                .collect(Collectors.toList());
    }

    public List<AlertResponse> getAlertsByLevel(Alert.AlertLevel level) {
        return alertRepository.findByLevelAndIsResolvedFalseOrderByTimestampDesc(level)
                .stream()
                .map(AlertResponse::fromAlert)
                .collect(Collectors.toList());
    }

    public AlertResponse createOccupancyAlert(Zone zone, String message) {
        Alert alert = new Alert();
        alert.setZone(zone);
        alert.setMessage(message);
        alert.setOccupancyCount(zone.getCurrentOccupancy());
        alert.setCapacityPercentage(zone.getDensityPercentage());

        // Determine alert level based on zone status
        switch (zone.getStatus()) {
            case WARNING:
                alert.setLevel(Alert.AlertLevel.WARNING);
                alert.setType(Alert.AlertType.OCCUPANCY_HIGH);
                break;
            case CRITICAL:
                alert.setLevel(Alert.AlertLevel.CRITICAL);
                alert.setType(Alert.AlertType.OCCUPANCY_CRITICAL);
                break;
            case EMERGENCY:
                alert.setLevel(Alert.AlertLevel.EMERGENCY);
                alert.setType(Alert.AlertType.EVACUATION_REQUIRED);
                break;
            default:
                alert.setLevel(Alert.AlertLevel.INFO);
                alert.setType(Alert.AlertType.SYSTEM_ALERT);
        }

        Alert savedAlert = alertRepository.save(alert);

        log.warn("🚨 ALERT CREATED - Level: {}, Zone: {}, Message: {}", 
                savedAlert.getLevel(), zone.getName(), message);

        return AlertResponse.fromAlert(savedAlert);
    }

    public Alert createEmergencyAlert(Zone zone, String reason) {
        Alert alert = new Alert();
        alert.setZone(zone);
        alert.setLevel(Alert.AlertLevel.EMERGENCY);
        alert.setType(Alert.AlertType.EVACUATION_REQUIRED);
        alert.setMessage("EMERGENCY EVACUATION REQUIRED - " + reason);
        alert.setOccupancyCount(zone.getCurrentOccupancy());
        alert.setCapacityPercentage(zone.getDensityPercentage());

        Alert savedAlert = alertRepository.save(alert);

        log.error("🚨🚨 EMERGENCY ALERT - Zone: {}, Reason: {}", zone.getName(), reason);

        return savedAlert;
    }

    public boolean resolveAlert(Long alertId, String resolverName) {
        Optional<Alert> optionalAlert = alertRepository.findById(alertId);

        if (optionalAlert.isEmpty()) {
            log.warn("Alert not found with id: {}", alertId);
            return false;
        }

        Alert alert = optionalAlert.get();
        alert.resolve(resolverName);
        alertRepository.save(alert);

        log.info("Alert resolved by {}: {}", resolverName, alert.getMessage());
        return true;
    }

    public List<AlertResponse> getAlertHistory(LocalDateTime startDate, LocalDateTime endDate) {
        return alertRepository.findAlertsBetweenDates(startDate, endDate)
                .stream()
                .map(AlertResponse::fromAlert)
                .collect(Collectors.toList());
    }

    public long getActiveEmergencyAlertCount() {
        return alertRepository.countActiveEmergencyAlerts();
    }

    public List<AlertResponse> getEmergencyAlerts() {
        return alertRepository.findByLevelAndIsResolvedFalseOrderByTimestampDesc(Alert.AlertLevel.EMERGENCY)
                .stream()
                .map(AlertResponse::fromAlert)
                .collect(Collectors.toList());
    }
}