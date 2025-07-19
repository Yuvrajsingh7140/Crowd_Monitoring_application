package com.example.crowdmonitoring;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.socket.config.annotation.EnableWebSocket;

@SpringBootApplication
@EnableScheduling
@EnableWebSocket
public class CrowdMonitoringSystemApplication {

    public static void main(String[] args) {
        SpringApplication.run(CrowdMonitoringSystemApplication.class, args);
        System.out.println("\n\n=== Crowd Monitoring System Started Successfully ===");
        System.out.println("🌐 Dashboard: http://localhost:8080");
        System.out.println("🔧 H2 Console: http://localhost:8080/h2-console");
        System.out.println("📊 API Docs: http://localhost:8080/api");
        System.out.println("✅ System ready for crowd monitoring!");
    }
}