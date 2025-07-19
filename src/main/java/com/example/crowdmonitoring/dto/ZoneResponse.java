package com.example.crowdmonitoring.dto;

import com.example.crowdmonitoring.model.Zone;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ZoneResponse {

    private Long id;
    private String name;
    private Integer capacity;
    private Integer currentOccupancy;
    private Double densityPercentage;
    private Zone.ZoneStatus status;
    private LocalDateTime lastUpdated;
    private String description;
    private String locationCoordinates;
    private String emergencyContact;
    private Boolean isOverCapacity;
    private Boolean isDangerous;

    public static ZoneResponse fromZone(Zone zone) {
        ZoneResponse response = new ZoneResponse();
        response.setId(zone.getId());
        response.setName(zone.getName());
        response.setCapacity(zone.getCapacity());
        response.setCurrentOccupancy(zone.getCurrentOccupancy());
        response.setDensityPercentage(zone.getDensityPercentage());
        response.setStatus(zone.getStatus());
        response.setLastUpdated(zone.getLastUpdated());
        response.setDescription(zone.getDescription());
        response.setLocationCoordinates(zone.getLocationCoordinates());
        response.setEmergencyContact(zone.getEmergencyContact());
        response.setIsOverCapacity(zone.isOverCapacity());
        response.setIsDangerous(zone.isDangerous());
        return response;
    }
}