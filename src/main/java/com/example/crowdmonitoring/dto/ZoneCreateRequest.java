package com.example.crowdmonitoring.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ZoneCreateRequest {

    @NotBlank(message = "Zone name is required")
    @Size(min = 2, max = 100, message = "Zone name must be between 2 and 100 characters")
    private String name;

    @NotNull(message = "Capacity is required")
    @Min(value = 1, message = "Capacity must be at least 1")
    @Max(value = 100000, message = "Capacity cannot exceed 100,000")
    private Integer capacity;

    @Size(max = 500, message = "Description cannot exceed 500 characters")
    private String description;

    private String locationCoordinates;
    private String emergencyContact;
}