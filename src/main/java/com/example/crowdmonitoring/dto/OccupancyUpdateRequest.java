package com.example.crowdmonitoring.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OccupancyUpdateRequest {

    @NotNull(message = "Occupancy count is required")
    @Min(value = 0, message = "Occupancy cannot be negative")
    private Integer occupancy;

    private String sensorId;
    private String source = "manual";
}