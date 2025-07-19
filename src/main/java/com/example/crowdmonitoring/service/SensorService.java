package com.example.crowdmonitoring.service;

import com.example.crowdmonitoring.model.Sensor;
import com.example.crowdmonitoring.model.Zone;
import com.example.crowdmonitoring.repository.SensorRepository;
import com.example.crowdmonitoring.repository.ZoneRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class SensorService {

    private final SensorRepository sensorRepository;
    private final ZoneRepository zoneRepository;
    private final ZoneService zoneService;

    public List<Sensor> getAllSensors() {
        return sensorRepository.findAll();
    }

    public List<Sensor> getActiveSensors() {
        return sensorRepository.findByIsActiveTrue();
    }

    public List<Sensor> getSensorsByZone(Long zoneId) {
        Optional<Zone> zone = zoneRepository.findById(zoneId);
        return zone.map(sensorRepository::findByZone).orElse(List.of());
    }

    public Optional<Sensor> getSensorById(Long sensorId) {
        return sensorRepository.findById(sensorId);
    }

    public Optional<Sensor> getSensorBySensorId(String sensorId) {
        return sensorRepository.findBySensorId(sensorId);
    }

    public Sensor createSensor(Sensor sensor) {
        Sensor savedSensor = sensorRepository.save(sensor);
        log.info("Created new sensor: {} for zone: {}", 
                savedSensor.getName(), savedSensor.getZone().getName());
        return savedSensor;
    }

    public boolean updateSensorReading(String sensorId, Integer reading) {
        Optional<Sensor> optionalSensor = sensorRepository.findBySensorId(sensorId);

        if (optionalSensor.isEmpty()) {
            log.warn("Sensor not found with sensorId: {}", sensorId);
            return false;
        }

        Sensor sensor = optionalSensor.get();
        sensor.updateReading(reading);
        sensorRepository.save(sensor);

        log.debug("Updated reading for sensor {}: {}", sensorId, reading);
        return true;
    }

    public boolean deactivateSensor(Long sensorId) {
        Optional<Sensor> optionalSensor = sensorRepository.findById(sensorId);

        if (optionalSensor.isEmpty()) {
            return false;
        }

        Sensor sensor = optionalSensor.get();
        sensor.setIsActive(false);
        sensorRepository.save(sensor);

        log.info("Deactivated sensor: {}", sensor.getName());
        return true;
    }

    public long getInactiveSensorCount() {
        return sensorRepository.countInactiveSensors();
    }
}