package com.example.crowdmonitoring.controller;

import com.example.crowdmonitoring.dto.AlertResponse;
import com.example.crowdmonitoring.model.Alert;
import com.example.crowdmonitoring.service.AlertService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/alerts")
@RequiredArgsConstructor
@CrossOrigin
@Slf4j
public class AlertController {

    private final AlertService alertService;

    @GetMapping
    public ResponseEntity<List<AlertResponse>> getAllActiveAlerts() {
        List<AlertResponse> alerts = alertService.getAllActiveAlerts();
        return ResponseEntity.ok(alerts);
    }

    @GetMapping("/level/{level}")
    public ResponseEntity<List<AlertResponse>> getAlertsByLevel(@PathVariable String level) {
        try {
            Alert.AlertLevel alertLevel = Alert.AlertLevel.valueOf(level.toUpperCase());
            List<AlertResponse> alerts = alertService.getAlertsByLevel(alertLevel);
            return ResponseEntity.ok(alerts);
        } catch (IllegalArgumentException e) {
            log.error("Invalid alert level: {}", level);
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/emergency")
    public ResponseEntity<List<AlertResponse>> getEmergencyAlerts() {
        List<AlertResponse> emergencyAlerts = alertService.getEmergencyAlerts();
        return ResponseEntity.ok(emergencyAlerts);
    }

    @PostMapping("/{id}/resolve")
    public ResponseEntity<Void> resolveAlert(
            @PathVariable Long id, 
            @RequestParam(defaultValue = "System") String resolverName) {

        boolean resolved = alertService.resolveAlert(id, resolverName);
        return resolved ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
    }

    @GetMapping("/history")
    public ResponseEntity<List<AlertResponse>> getAlertHistory(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {

        List<AlertResponse> alertHistory = alertService.getAlertHistory(startDate, endDate);
        return ResponseEntity.ok(alertHistory);
    }

    @GetMapping("/stats/emergency-count")
    public ResponseEntity<Long> getActiveEmergencyAlertCount() {
        long count = alertService.getActiveEmergencyAlertCount();
        return ResponseEntity.ok(count);
    }
}