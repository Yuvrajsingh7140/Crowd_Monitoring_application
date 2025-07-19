-- Sample data for Crowd Monitoring System

-- Insert sample zones
INSERT INTO zones (name, capacity, current_occupancy, created_at, last_updated, status, density_percentage, description, location_coordinates, emergency_contact)
VALUES 
('Main Auditorium', 1000, 450, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'CAUTION', 45.0, 'Main event auditorium with 1000 seat capacity', '12.9716,77.5946', '+91-98765-43210'),
('Exhibition Hall A', 800, 680, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'WARNING', 85.0, 'Large exhibition hall for trade shows', '12.9716,77.5946', '+91-98765-43211'),
('Food Court', 300, 290, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'CRITICAL', 96.7, 'Central food court area', '12.9716,77.5946', '+91-98765-43212'),
('Parking Area', 500, 125, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'SAFE', 25.0, 'Main parking area', '12.9716,77.5946', '+91-98765-43213'),
('Conference Room B', 200, 50, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'SAFE', 25.0, 'Medium conference room', '12.9716,77.5946', '+91-98765-43214');

-- Insert sample sensors
INSERT INTO sensors (name, zone_id, type, sensor_id, is_active, last_reading, last_reading_time, installation_date, description, location_details)
VALUES 
('Thermal Camera 1', 1, 'THERMAL_CAMERA', 'TC001', true, 450, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'Main entrance thermal camera', 'Main entrance - North side'),
('People Counter 2', 2, 'PEOPLE_COUNTER', 'PC002', true, 680, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'Exhibition hall people counter', 'Exhibition Hall A - Main entrance'),
('Motion Detector 3', 3, 'MOTION_DETECTOR', 'MD003', true, 290, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'Food court motion detector', 'Food Court - Center ceiling'),
('Infrared Sensor 4', 4, 'INFRARED_SENSOR', 'IR004', true, 125, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'Parking area sensor', 'Parking - Gate 1'),
('Thermal Camera 5', 5, 'THERMAL_CAMERA', 'TC005', false, 0, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'Conference room camera', 'Conference Room B - Rear wall');

-- Insert sample alerts
INSERT INTO alerts (zone_id, level, message, timestamp, is_resolved, resolved_at, resolved_by, type, occupancy_count, capacity_percentage)
VALUES 
(3, 'CRITICAL', 'Food Court occupancy critical: 290/300 people (96.7%) - Source: Motion Detector', CURRENT_TIMESTAMP, false, null, null, 'OCCUPANCY_CRITICAL', 290, 96.7),
(2, 'WARNING', 'Exhibition Hall A occupancy high: 680/800 people (85.0%) - Source: People Counter', DATEADD('MINUTE', -15, CURRENT_TIMESTAMP), true, DATEADD('MINUTE', -5, CURRENT_TIMESTAMP), 'Security Team', 'OCCUPANCY_HIGH', 680, 85.0),
(1, 'INFO', 'Main Auditorium status update: 450/1000 people (45.0%)', DATEADD('HOUR', -1, CURRENT_TIMESTAMP), true, DATEADD('MINUTE', -30, CURRENT_TIMESTAMP), 'Admin', 'SYSTEM_ALERT', 450, 45.0);