package com.example.crowdmonitoring.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "alerts")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Alert {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "zone_id", nullable = false)
    private Zone zone;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AlertLevel level;

    @NotBlank(message = "Alert message is required")
    @Size(max = 1000, message = "Alert message cannot exceed 1000 characters")
    @Column(nullable = false, length = 1000)
    private String message;

    @Column(nullable = false)
    private LocalDateTime timestamp;

    @Column(name = "resolved_at")
    private LocalDateTime resolvedAt;

    @Column(name = "is_resolved")
    private Boolean isResolved = false;

    @Column(name = "resolved_by")
    private String resolvedBy;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AlertType type;

    @Column(name = "occupancy_count")
    private Integer occupancyCount;

    @Column(name = "capacity_percentage")
    private Double capacityPercentage;

    public enum AlertLevel {
        INFO,       // Informational alerts
        WARNING,    // Caution level alerts
        CRITICAL,   // Critical level alerts
        EMERGENCY   // Emergency level alerts
    }

    public enum AlertType {
        OCCUPANCY_HIGH,
        OCCUPANCY_CRITICAL,
        EVACUATION_REQUIRED,
        SENSOR_FAILURE,
        SYSTEM_ALERT
    }

    @PrePersist
    protected void onCreate() {
        timestamp = LocalDateTime.now();
    }

    public void resolve(String resolverName) {
        this.isResolved = true;
        this.resolvedAt = LocalDateTime.now();
        this.resolvedBy = resolverName;
    }

    public boolean isActive() {
        return !isResolved;
    }
}