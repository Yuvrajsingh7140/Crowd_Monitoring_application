package com.example.crowdmonitoring;

import com.example.crowdmonitoring.dto.OccupancyUpdateRequest;
import com.example.crowdmonitoring.dto.ZoneCreateRequest;
import com.example.crowdmonitoring.dto.ZoneResponse;
import com.example.crowdmonitoring.model.Zone;
import com.example.crowdmonitoring.service.ZoneService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class ZoneServiceTest {

    @Autowired
    private ZoneService zoneService;

    @Test
    public void testCreateZone() {
        // Arrange
        ZoneCreateRequest request = new ZoneCreateRequest();
        request.setName("Test Zone");
        request.setCapacity(100);
        request.setDescription("Test zone for unit testing");

        // Act
        ZoneResponse response = zoneService.createZone(request);

        // Assert
        assertNotNull(response);
        assertEquals("Test Zone", response.getName());
        assertEquals(100, response.getCapacity());
        assertEquals(0, response.getCurrentOccupancy());
        assertEquals(Zone.ZoneStatus.SAFE, response.getStatus());
    }

    @Test
    public void testUpdateOccupancy() {
        // Arrange
        ZoneCreateRequest createRequest = new ZoneCreateRequest();
        createRequest.setName("Test Zone for Occupancy");
        createRequest.setCapacity(100);
        createRequest.setDescription("Test zone for occupancy testing");

        ZoneResponse createdZone = zoneService.createZone(createRequest);

        OccupancyUpdateRequest updateRequest = new OccupancyUpdateRequest();
        updateRequest.setOccupancy(80);
        updateRequest.setSource("test");

        // Act
        var updatedZone = zoneService.updateOccupancy(createdZone.getId(), updateRequest);

        // Assert
        assertTrue(updatedZone.isPresent());
        assertEquals(80, updatedZone.get().getCurrentOccupancy());
        assertEquals(80.0, updatedZone.get().getDensityPercentage());
        assertEquals(Zone.ZoneStatus.WARNING, updatedZone.get().getStatus());
    }

    @Test
    public void testZoneStatusTransitions() {
        // Test that zone status changes correctly based on occupancy percentage
        ZoneCreateRequest request = new ZoneCreateRequest();
        request.setName("Status Test Zone");
        request.setCapacity(1000);
        request.setDescription("Zone for testing status transitions");

        ZoneResponse zone = zoneService.createZone(request);
        Long zoneId = zone.getId();

        // Test SAFE status (0-50%)
        OccupancyUpdateRequest safeUpdate = new OccupancyUpdateRequest();
        safeUpdate.setOccupancy(400);
        safeUpdate.setSource("test");
        var safeZone = zoneService.updateOccupancy(zoneId, safeUpdate);
        assertTrue(safeZone.isPresent());
        assertEquals(Zone.ZoneStatus.SAFE, safeZone.get().getStatus());

        // Test CAUTION status (51-69%)
        OccupancyUpdateRequest cautionUpdate = new OccupancyUpdateRequest();
        cautionUpdate.setOccupancy(600);
        cautionUpdate.setSource("test");
        var cautionZone = zoneService.updateOccupancy(zoneId, cautionUpdate);
        assertTrue(cautionZone.isPresent());
        assertEquals(Zone.ZoneStatus.CAUTION, cautionZone.get().getStatus());

        // Test WARNING status (70-84%)
        OccupancyUpdateRequest warningUpdate = new OccupancyUpdateRequest();
        warningUpdate.setOccupancy(800);
        warningUpdate.setSource("test");
        var warningZone = zoneService.updateOccupancy(zoneId, warningUpdate);
        assertTrue(warningZone.isPresent());
        assertEquals(Zone.ZoneStatus.WARNING, warningZone.get().getStatus());

        // Test CRITICAL status (85-95%)
        OccupancyUpdateRequest criticalUpdate = new OccupancyUpdateRequest();
        criticalUpdate.setOccupancy(900);
        criticalUpdate.setSource("test");
        var criticalZone = zoneService.updateOccupancy(zoneId, criticalUpdate);
        assertTrue(criticalZone.isPresent());
        assertEquals(Zone.ZoneStatus.CRITICAL, criticalZone.get().getStatus());

        // Test EMERGENCY status (96-100%+)
        OccupancyUpdateRequest emergencyUpdate = new OccupancyUpdateRequest();
        emergencyUpdate.setOccupancy(980);
        emergencyUpdate.setSource("test");
        var emergencyZone = zoneService.updateOccupancy(zoneId, emergencyUpdate);
        assertTrue(emergencyZone.isPresent());
        assertEquals(Zone.ZoneStatus.EMERGENCY, emergencyZone.get().getStatus());
    }
}