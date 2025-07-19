# 🔌 Crowd Monitoring System API Documentation

## Overview
This document provides comprehensive information about the RESTful API endpoints available in the Crowd Monitoring System.

## Base URL
```
http://localhost:8080/api
```

## Authentication
Currently, the API does not require authentication. In production, implement appropriate authentication mechanisms.

## Response Format
All API responses follow a consistent JSON format:

### Success Response
```json
{
  "id": 1,
  "name": "Main Auditorium",
  "capacity": 1000,
  "currentOccupancy": 450,
  "status": "CAUTION",
  "densityPercentage": 45.0,
  "lastUpdated": "2025-01-19T10:30:00"
}
```

### Error Response
```json
{
  "timestamp": "2025-01-19T10:30:00",
  "status": 400,
  "error": "Bad Request",
  "message": "Invalid input data",
  "path": "/api/zones"
}
```

## Endpoints

### Zone Management

#### GET /zones
Get all zones with their current status.

**Response:**
```json
[
  {
    "id": 1,
    "name": "Main Auditorium",
    "capacity": 1000,
    "currentOccupancy": 450,
    "densityPercentage": 45.0,
    "status": "CAUTION",
    "lastUpdated": "2025-01-19T10:30:00",
    "description": "Main event auditorium",
    "isOverCapacity": false,
    "isDangerous": false
  }
]
```

#### POST /zones
Create a new zone.

**Request Body:**
```json
{
  "name": "Conference Room A",
  "capacity": 200,
  "description": "Large conference room",
  "locationCoordinates": "12.9716,77.5946",
  "emergencyContact": "+91-98765-43210"
}
```

#### POST /zones/{id}/occupancy
Update the occupancy count for a specific zone.

**Request Body:**
```json
{
  "occupancy": 450,
  "source": "thermal_camera_1"
}
```

### Alert Management

#### GET /alerts
Get all active alerts.

**Response:**
```json
[
  {
    "id": 1,
    "zoneId": 1,
    "zoneName": "Main Auditorium",
    "level": "WARNING",
    "message": "Occupancy approaching capacity limit",
    "timestamp": "2025-01-19T10:30:00",
    "isResolved": false,
    "type": "OCCUPANCY_HIGH"
  }
]
```

#### POST /alerts/{id}/resolve
Resolve an active alert.

**Parameters:**
- `resolverName` (optional): Name of the person resolving the alert

### Dashboard Analytics

#### GET /dashboard/overview
Get comprehensive dashboard data.

**Response:**
```json
{
  "zones": {
    "total": 5,
    "dangerous": 1,
    "emergency": 0,
    "allZones": [...]
  },
  "alerts": {
    "active": 2,
    "emergency": 0,
    "recentAlerts": [...]
  },
  "sensors": {
    "total": 10,
    "active": 8,
    "inactive": 2
  },
  "status": "WARNING",
  "lastUpdate": "2025-01-19T10:30:00"
}
```

## Status Codes

| Code | Description |
|------|-------------|
| 200  | OK - Request successful |
| 201  | Created - Resource created successfully |
| 400  | Bad Request - Invalid input data |
| 404  | Not Found - Resource not found |
| 500  | Internal Server Error - Server error |

## Rate Limiting
Currently no rate limiting is implemented. Consider implementing rate limiting for production use.

## WebSocket Support
Real-time updates are available via WebSocket connection:
```
ws://localhost:8080/ws/crowd-monitoring
```

## Sample cURL Commands

### Get All Zones
```bash
curl -X GET http://localhost:8080/api/zones
```

### Create Zone
```bash
curl -X POST http://localhost:8080/api/zones \
  -H "Content-Type: application/json" \
  -d '{"name":"Test Zone","capacity":100}'
```

### Update Occupancy
```bash
curl -X POST http://localhost:8080/api/zones/1/occupancy \
  -H "Content-Type: application/json" \
  -d '{"occupancy":75,"source":"test"}'
```
