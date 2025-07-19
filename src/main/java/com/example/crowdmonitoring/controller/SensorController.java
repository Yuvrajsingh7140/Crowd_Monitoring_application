package com.example.crowdmonitoring.controller;

import com.example.crowdmonitoring.model.Sensor;
import com.example.crowdmonitoring.service.SensorService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/sensors")
@RequiredArgsConstructor
@CrossOrigin
@Slf4j
public class SensorController {

    private final SensorService sensorService;

    @GetMapping
    public ResponseEntity<List<Sensor>> getAllSensors() {
        List<Sensor> sensors = sensorService.getAllSensors();
        return ResponseEntity.ok(sensors);
    }

    @GetMapping("/active")
    public ResponseEntity<List<Sensor>> getActiveSensors() {
        List<Sensor> activeSensors = sensorService.getActiveSensors();
        return ResponseEntity.ok(activeSensors);
    }

    @GetMapping("/zone/{zoneId}")
    public ResponseEntity<List<Sensor>> getSensorsByZone(@PathVariable Long zoneId) {
        List<Sensor> sensors = sensorService.getSensorsByZone(zoneId);
        return ResponseEntity.ok(sensors);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Sensor> getSensorById(@PathVariable Long id) {
        Optional<Sensor> sensor = sensorService.getSensorById(id);
        return sensor.map(ResponseEntity::ok)
                     .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Sensor> createSensor(@RequestBody Sensor sensor) {
        try {
            Sensor createdSensor = sensorService.createSensor(sensor);
            return ResponseEntity.ok(createdSensor);
        } catch (Exception e) {
            log.error("Error creating sensor: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/{sensorId}/reading")
    public ResponseEntity<Map<String, String>> updateSensorReading(
            @PathVariable String sensorId, 
            @RequestParam Integer reading) {

        boolean updated = sensorService.updateSensorReading(sensorId, reading);

        if (updated) {
            return ResponseEntity.ok(Map.of("status", "success", "message", "Reading updated"));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/{id}/deactivate")
    public ResponseEntity<Map<String, String>> deactivateSensor(@PathVariable Long id) {
        boolean deactivated = sensorService.deactivateSensor(id);

        if (deactivated) {
            return ResponseEntity.ok(Map.of("status", "success", "message", "Sensor deactivated"));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/stats/inactive-count")
    public ResponseEntity<Long> getInactiveSensorCount() {
        long count = sensorService.getInactiveSensorCount();
        return ResponseEntity.ok(count);
    }
}