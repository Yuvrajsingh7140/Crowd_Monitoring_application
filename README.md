# 🚨 Crowd Monitoring System (Stampede Avoidance)

A comprehensive **Java Spring Boot** application designed for **real-time crowd monitoring** and **stampede prevention** in large gatherings, public events, and high-traffic venues.

## 🎯 Features

### ✅ Core Functionality
- **Real-time Crowd Density Monitoring** - Continuous tracking of people count across multiple zones
- **Automated Stampede Prevention** - AI-powered early warning system with configurable thresholds
- **Multi-zone Management** - Independent monitoring of entrances, main areas, and emergency exits
- **Emergency Response Integration** - Automated evacuation protocols and emergency service notifications
- **IoT Sensor Integration** - Seamless connectivity with CCTV cameras, thermal sensors, and people counters

### 🔧 Technical Features
- **RESTful APIs** - Comprehensive REST endpoints for all system operations
- **Real-time Dashboard** - Live web-based monitoring interface with auto-refresh
- **WebSocket Support** - Real-time bidirectional communication for instant updates
- **Flexible Alerting System** - Multi-level alert system with automatic escalation
- **Database Integration** - Support for both H2 (development) and MySQL (production)

## 🏗️ System Architecture

### Zone Status Levels
- **SAFE** (0-50% capacity) - ✅ Normal operations
- **CAUTION** (51-69% capacity) - ⚠️ Increased monitoring
- **WARNING** (70-84% capacity) - 🔶 High density detected
- **CRITICAL** (85-95% capacity) - 🔴 Dangerous crowd levels
- **EMERGENCY** (96-100%+ capacity) - 🚨 Evacuation required

### Alert Levels
1. **INFO** - Routine status updates
2. **WARNING** - Increased crowd density detected  
3. **CRITICAL** - Dangerous crowd levels requiring immediate attention
4. **EMERGENCY** - Evacuation or emergency response required

## 🚀 Quick Start

### Prerequisites
- ☕ **Java 17** or higher
- 📦 **Maven 3.9+**
- 🗄️ **H2 Database** (included) or **MySQL 8.0+** (for production)

### Installation & Setup

1. **Extract the project:**
   ```bash
   unzip crowd-monitoring-system.zip
   cd crowd-monitoring-system
   ```

2. **Run the application:**
   ```bash
   ./mvnw spring-boot:run
   ```

   *On Windows:*
   ```cmd
   mvnw.cmd spring-boot:run
   ```

3. **Access the system:**
   - 🌐 **Dashboard:** http://localhost:8080
   - 🗄️ **Database Console:** http://localhost:8080/h2-console
   - 📊 **API Documentation:** http://localhost:8080/api

### H2 Database Connection
- **URL:** `jdbc:h2:mem:crowd_monitoring_db`
- **Username:** `sa`
- **Password:** (leave empty)

## 📊 Dashboard

The system includes a **real-time web dashboard** with:

- **Live Zone Status** - Color-coded zone cards with occupancy levels
- **Alert Management** - Active alerts with severity indicators  
- **System Statistics** - Overall system health and metrics
- **Auto-refresh** - Updates every 30 seconds automatically

![Dashboard Preview](dashboard-preview.png)

## 🔌 API Endpoints

### Zone Management
```http
GET    /api/zones                    # Get all zones
GET    /api/zones/{id}               # Get specific zone
POST   /api/zones                    # Create new zone
POST   /api/zones/{id}/occupancy     # Update zone occupancy
DELETE /api/zones/{id}               # Delete zone
GET    /api/zones/dangerous          # Get dangerous zones
GET    /api/zones/status/{status}    # Get zones by status
```

### Alert Management
```http
GET    /api/alerts                   # Get all active alerts
GET    /api/alerts/level/{level}     # Get alerts by level
GET    /api/alerts/emergency         # Get emergency alerts
POST   /api/alerts/{id}/resolve      # Resolve alert
GET    /api/alerts/history           # Get alert history
```

### Dashboard & Analytics
```http
GET    /api/dashboard/overview       # Complete dashboard data
GET    /api/dashboard/zones-summary  # Zone statistics summary
```

### Sensor Integration
```http
GET    /api/sensors                  # Get all sensors
GET    /api/sensors/active           # Get active sensors
GET    /api/sensors/zone/{zoneId}    # Get sensors by zone
POST   /api/sensors/{id}/reading     # Update sensor reading
POST   /api/sensors/{id}/deactivate  # Deactivate sensor
```

## 📝 API Usage Examples

### Create a New Zone
```bash
curl -X POST http://localhost:8080/api/zones \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Main Auditorium",
    "capacity": 1000,
    "description": "Primary event venue",
    "emergencyContact": "+91-98765-43210"
  }'
```

### Update Zone Occupancy
```bash
curl -X POST http://localhost:8080/api/zones/1/occupancy \
  -H "Content-Type: application/json" \
  -d '{
    "occupancy": 850,
    "source": "thermal_camera_1"
  }'
```

### Get Dashboard Overview
```bash
curl http://localhost:8080/api/dashboard/overview
```

## 🗄️ Database Schema

### Core Tables
- **zones** - Zone definitions with capacity and current occupancy
- **alerts** - Alert records with levels and resolution status
- **sensors** - IoT sensor configurations and readings

### Sample Data
The application includes sample data with:
- 5 pre-configured zones (Auditorium, Exhibition Hall, Food Court, etc.)
- 5 simulated sensors with different types
- Historical alert records for testing

## 🧪 Testing

### Run Unit Tests
```bash
./mvnw test
```

### Test Coverage
- **ZoneServiceTest** - Zone management and status transitions
- **ApplicationTests** - Spring Boot context loading
- **Integration Tests** - Full API endpoint testing

## 📋 Configuration

### Database Configuration (Production)
```properties
# MySQL Configuration (for production)
spring.datasource.url=jdbc:mysql://localhost:3306/crowd_monitoring
spring.datasource.username=your_username
spring.datasource.password=your_password
spring.jpa.hibernate.ddl-auto=update
```

### Alert Configuration
```properties
# Custom Alert Settings
crowd-monitoring.alert.email.enabled=true
crowd-monitoring.alert.sms.enabled=true
crowd-monitoring.websocket.enabled=true
crowd-monitoring.auto-alerts.enabled=true
```

## 🔒 Security Features

- **Input Validation** - All API inputs are validated using Bean Validation
- **SQL Injection Protection** - JPA/Hibernate prevents SQL injection attacks
- **CORS Configuration** - Configurable cross-origin resource sharing
- **Error Handling** - Comprehensive error responses with appropriate HTTP status codes

## 🎛️ Monitoring & Health

### Actuator Endpoints
- `/actuator/health` - Application health status
- `/actuator/info` - Application information
- `/actuator/metrics` - Application metrics

### Logging
- **Debug Level** - Comprehensive logging for development
- **SQL Logging** - Database query logging for troubleshooting
- **Custom Loggers** - Application-specific logging configuration

## 🚀 Deployment

### Docker Support
```dockerfile
FROM openjdk:17-jre-slim
COPY target/crowd-monitoring-system-1.0.0.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/app.jar"]
```

### Production Deployment
1. **Build for production:**
   ```bash
   ./mvnw clean package -Pprod
   ```

2. **Configure production database**
3. **Set environment variables**
4. **Deploy to your preferred platform (AWS, Heroku, etc.)**

## 📈 Future Enhancements

### Planned Features
- [ ] **Machine Learning** - Predictive crowd flow analysis
- [ ] **Mobile App** - Native mobile applications for field operators
- [ ] **SMS/Email Alerts** - Multi-channel notification system
- [ ] **CCTV Integration** - Real-time video analytics
- [ ] **Heat Maps** - Visual crowd density mapping
- [ ] **Evacuation Planning** - Automated evacuation route optimization

### Integration Possibilities
- **Emergency Services** - Direct integration with fire/police departments
- **Public Address Systems** - Automated announcements
- **Digital Signage** - Dynamic crowd guidance displays
- **Access Control** - Integration with turnstiles and gates

## 🤝 Contributing

1. Fork the repository
2. Create your feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

## 📞 Support

For support, issues, or feature requests:
- 📧 Email: support@crowdmonitoring.com
- 🐛 Issues: GitHub Issues
- 📖 Documentation: Wiki pages

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## 🙏 Acknowledgments

- **Spring Boot Team** - For the excellent framework
- **H2 Database** - For the embedded database solution
- **Bootstrap** - For responsive UI components
- **Font Awesome** - For iconography

---

### 💡 **Ready to prevent stampedes and save lives!** 

🚨 **Start monitoring crowds safely with this comprehensive Spring Boot solution** 🚨

---

*Last Updated: January 2025*
*Version: 1.0.0*