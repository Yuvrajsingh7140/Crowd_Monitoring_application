package com.example.crowdmonitoring.repository;

import com.example.crowdmonitoring.model.Sensor;
import com.example.crowdmonitoring.model.Zone;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SensorRepository extends JpaRepository<Sensor, Long> {

    List<Sensor> findByZone(Zone zone);

    List<Sensor> findByIsActiveTrue();

    List<Sensor> findByType(Sensor.SensorType type);

    Optional<Sensor> findBySensorId(String sensorId);

    @Query("SELECT s FROM Sensor s WHERE s.isActive = true AND s.zone = :zone")
    List<Sensor> findActiveSensorsByZone(Zone zone);

    @Query("SELECT COUNT(s) FROM Sensor s WHERE s.isActive = false")
    Long countInactiveSensors();
}