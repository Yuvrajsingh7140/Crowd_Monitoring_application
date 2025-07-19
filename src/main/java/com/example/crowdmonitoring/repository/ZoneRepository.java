package com.example.crowdmonitoring.repository;

import com.example.crowdmonitoring.model.Zone;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ZoneRepository extends JpaRepository<Zone, Long> {

    List<Zone> findByStatus(Zone.ZoneStatus status);

    @Query("SELECT z FROM Zone z WHERE z.currentOccupancy > z.capacity")
    List<Zone> findOverCapacityZones();

    @Query("SELECT z FROM Zone z WHERE z.densityPercentage > :percentage")
    List<Zone> findZonesAboveCapacity(Double percentage);

    Optional<Zone> findByName(String name);

    @Query("SELECT z FROM Zone z WHERE z.status IN ('CRITICAL', 'EMERGENCY')")
    List<Zone> findDangerousZones();

    @Query("SELECT COUNT(z) FROM Zone z WHERE z.status = 'EMERGENCY'")
    Long countEmergencyZones();
}