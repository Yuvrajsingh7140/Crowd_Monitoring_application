package com.example.crowdmonitoring.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "zones")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Zone {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Zone name is required")
    @Size(min = 2, max = 100, message = "Zone name must be between 2 and 100 characters")
    @Column(nullable = false)
    private String name;

    @NotNull(message = "Capacity is required")
    @Min(value = 1, message = "Capacity must be at least 1")
    @Max(value = 100000, message = "Capacity cannot exceed 100,000")
    private Integer capacity;

    @Min(value = 0, message = "Current occupancy cannot be negative")
    private Integer currentOccupancy = 0;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime lastUpdated;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ZoneStatus status = ZoneStatus.SAFE;

    @Column(name = "density_percentage")
    private Double densityPercentage = 0.0;

    @Column(length = 500)
    private String description;

    @Column(name = "location_coordinates")
    private String locationCoordinates;

    @Column(name = "emergency_contact")
    private String emergencyContact;

    public enum ZoneStatus {
        SAFE,       // 0-50% capacity
        CAUTION,    // 51-69% capacity  
        WARNING,    // 70-84% capacity
        CRITICAL,   // 85-95% capacity
        EMERGENCY   // 96-100% capacity
    }

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        lastUpdated = LocalDateTime.now();
        updateStatus();
    }

    @PreUpdate
    protected void onUpdate() {
        lastUpdated = LocalDateTime.now();
        updateStatus();
    }

    public void updateStatus() {
        if (capacity != null && capacity > 0) {
            densityPercentage = (currentOccupancy * 100.0) / capacity;

            if (densityPercentage >= 96) {
                status = ZoneStatus.EMERGENCY;
            } else if (densityPercentage >= 85) {
                status = ZoneStatus.CRITICAL;
            } else if (densityPercentage >= 70) {
                status = ZoneStatus.WARNING;
            } else if (densityPercentage >= 51) {
                status = ZoneStatus.CAUTION;
            } else {
                status = ZoneStatus.SAFE;
            }
        }
    }

    public boolean isOverCapacity() {
        return currentOccupancy > capacity;
    }

    public boolean isDangerous() {
        return status == ZoneStatus.CRITICAL || status == ZoneStatus.EMERGENCY;
    }
}