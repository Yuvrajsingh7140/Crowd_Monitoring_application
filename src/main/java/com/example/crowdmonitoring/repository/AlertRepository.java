package com.example.crowdmonitoring.repository;

import com.example.crowdmonitoring.model.Alert;
import com.example.crowdmonitoring.model.Zone;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AlertRepository extends JpaRepository<Alert, Long> {

    List<Alert> findByIsResolvedFalseOrderByTimestampDesc();

    List<Alert> findByZoneAndIsResolvedFalseOrderByTimestampDesc(Zone zone);

    List<Alert> findByLevelAndIsResolvedFalseOrderByTimestampDesc(Alert.AlertLevel level);

    @Query("SELECT a FROM Alert a WHERE a.timestamp BETWEEN :startDate AND :endDate ORDER BY a.timestamp DESC")
    List<Alert> findAlertsBetweenDates(@Param("startDate") LocalDateTime startDate, 
                                      @Param("endDate") LocalDateTime endDate);

    @Query("SELECT COUNT(a) FROM Alert a WHERE a.isResolved = false AND a.level = 'EMERGENCY'")
    Long countActiveEmergencyAlerts();

    List<Alert> findByTypeAndIsResolvedFalseOrderByTimestampDesc(Alert.AlertType type);
}