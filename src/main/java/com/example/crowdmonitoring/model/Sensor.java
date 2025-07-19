package com.example.crowdmonitoring.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "sensors")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Sensor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Sensor name is required")
    @Size(min = 2, max = 100, message = "Sensor name must be between 2 and 100 characters")
    @Column(nullable = false)
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "zone_id", nullable = false)
    private Zone zone;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SensorType type;

    @Column(name = "sensor_id", unique = true)
    private String sensorId;

    @Column(nullable = false)
    private Boolean isActive = true;

    @Column(name = "last_reading")
    private Integer lastReading;

    @Column(name = "last_reading_time")
    private LocalDateTime lastReadingTime;

    @Column(name = "installation_date")
    private LocalDateTime installationDate;

    @Column(length = 500)
    private String description;

    @Column(name = "location_details")
    private String locationDetails;

    public enum SensorType {
        THERMAL_CAMERA,
        PEOPLE_COUNTER,
        MOTION_DETECTOR,
        PRESSURE_SENSOR,
        INFRARED_SENSOR
    }

    @PrePersist
    protected void onCreate() {
        installationDate = LocalDateTime.now();
    }

    public void updateReading(Integer reading) {
        this.lastReading = reading;
        this.lastReadingTime = LocalDateTime.now();
    }
}