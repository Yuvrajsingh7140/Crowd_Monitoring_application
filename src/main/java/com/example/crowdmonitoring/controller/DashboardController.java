package com.example.crowdmonitoring.controller;

import com.example.crowdmonitoring.dto.AlertResponse;
import com.example.crowdmonitoring.dto.ZoneResponse;
import com.example.crowdmonitoring.service.AlertService;
import com.example.crowdmonitoring.service.SensorService;
import com.example.crowdmonitoring.service.ZoneService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
@CrossOrigin
@Slf4j
public class DashboardController {

    private final ZoneService zoneService;
    private final AlertService alertService;
    private final SensorService sensorService;

    @GetMapping("/overview")
    public ResponseEntity<Map<String, Object>> getDashboardOverview() {
        Map<String, Object> dashboard = new HashMap<>();

        // Zone statistics
        List<ZoneResponse> allZones = zoneService.getAllZones();
        List<ZoneResponse> dangerousZones = zoneService.getDangerousZones();
        long emergencyZoneCount = zoneService.getEmergencyZoneCount();

        // Alert statistics
        List<AlertResponse> activeAlerts = alertService.getAllActiveAlerts();
        List<AlertResponse> emergencyAlerts = alertService.getEmergencyAlerts();
        long activeEmergencyAlerts = alertService.getActiveEmergencyAlertCount();

        // Sensor statistics
        long inactiveSensors = sensorService.getInactiveSensorCount();
        int totalSensors = sensorService.getAllSensors().size();
        int activeSensors = sensorService.getActiveSensors().size();

        // Build dashboard data
        dashboard.put("zones", Map.of(
            "total", allZones.size(),
            "dangerous", dangerousZones.size(),
            "emergency", emergencyZoneCount,
            "allZones", allZones
        ));

        dashboard.put("alerts", Map.of(
            "active", activeAlerts.size(),
            "emergency", activeEmergencyAlerts,
            "recentAlerts", activeAlerts
        ));

        dashboard.put("sensors", Map.of(
            "total", totalSensors,
            "active", activeSensors,
            "inactive", inactiveSensors
        ));

        dashboard.put("status", determineSystemStatus(emergencyZoneCount, activeEmergencyAlerts));
        dashboard.put("lastUpdate", java.time.LocalDateTime.now());

        return ResponseEntity.ok(dashboard);
    }

    @GetMapping("/zones-summary")
    public ResponseEntity<Map<String, Object>> getZonesSummary() {
        List<ZoneResponse> allZones = zoneService.getAllZones();

        Map<String, Long> statusCounts = new HashMap<>();
        statusCounts.put("safe", 0L);
        statusCounts.put("caution", 0L);
        statusCounts.put("warning", 0L);
        statusCounts.put("critical", 0L);
        statusCounts.put("emergency", 0L);

        int totalOccupancy = 0;
        int totalCapacity = 0;

        for (ZoneResponse zone : allZones) {
            String status = zone.getStatus().name().toLowerCase();
            statusCounts.put(status, statusCounts.get(status) + 1);

            totalOccupancy += zone.getCurrentOccupancy();
            totalCapacity += zone.getCapacity();
        }

        double overallDensity = totalCapacity > 0 ? (totalOccupancy * 100.0 / totalCapacity) : 0;

        Map<String, Object> summary = new HashMap<>();
        summary.put("statusCounts", statusCounts);
        summary.put("totalZones", allZones.size());
        summary.put("totalOccupancy", totalOccupancy);
        summary.put("totalCapacity", totalCapacity);
        summary.put("overallDensity", Math.round(overallDensity * 10) / 10.0);

        return ResponseEntity.ok(summary);
    }

    private String determineSystemStatus(long emergencyZones, long emergencyAlerts) {
        if (emergencyZones > 0 || emergencyAlerts > 0) {
            return "EMERGENCY";
        } else if (zoneService.getDangerousZones().size() > 0) {
            return "WARNING";
        } else {
            return "NORMAL";
        }
    }
}