package com.av.apivisa.controller;

import com.av.apivisa.dto.HealthCheckResponseTO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/health")
@RequiredArgsConstructor
public class HealthController {

    @Value("${spring.profiles.active:unknown}")
    private String environment;

    @Value("${app.version:1.0.0}")
    private String version;

    @GetMapping
    public ResponseEntity<HealthCheckResponseTO> healthCheck() {
        HealthCheckResponseTO response = new HealthCheckResponseTO(
                "UP",
                LocalDateTime.now(),
                version,
                environment,
                new HealthCheckResponseTO.DatabaseHealth("UP", "Conectado com sucesso")
        );
        return ResponseEntity.ok(response);
    }

    @GetMapping("/ready")
    public ResponseEntity<String> readiness() {
        return ResponseEntity.ok("Ready");
    }

    @GetMapping("/live")
    public ResponseEntity<String> liveness() {
        return ResponseEntity.ok("Live");
    }
}