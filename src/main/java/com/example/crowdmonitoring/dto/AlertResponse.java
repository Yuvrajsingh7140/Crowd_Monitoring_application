package com.example.crowdmonitoring.dto;

import com.example.crowdmonitoring.model.Alert;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AlertResponse {

    private Long id;
    private Long zoneId;
    private String zoneName;
    private Alert.AlertLevel level;
    private String message;
    private LocalDateTime timestamp;
    private LocalDateTime resolvedAt;
    private Boolean isResolved;
    private String resolvedBy;
    private Alert.AlertType type;
    private Integer occupancyCount;
    private Double capacityPercentage;

    public static AlertResponse fromAlert(Alert alert) {
        AlertResponse response = new AlertResponse();
        response.setId(alert.getId());
        response.setZoneId(alert.getZone().getId());
        response.setZoneName(alert.getZone().getName());
        response.setLevel(alert.getLevel());
        response.setMessage(alert.getMessage());
        response.setTimestamp(alert.getTimestamp());
        response.setResolvedAt(alert.getResolvedAt());
        response.setIsResolved(alert.getIsResolved());
        response.setResolvedBy(alert.getResolvedBy());
        response.setType(alert.getType());
        response.setOccupancyCount(alert.getOccupancyCount());
        response.setCapacityPercentage(alert.getCapacityPercentage());
        return response;
    }
}