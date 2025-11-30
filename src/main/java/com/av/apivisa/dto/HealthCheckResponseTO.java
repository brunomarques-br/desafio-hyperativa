package com.av.apivisa.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class HealthCheckResponseTO {

    private String status;
    private LocalDateTime timestamp;
    private String version;
    private String environment;
    private DatabaseHealth database;

    @Data
    @AllArgsConstructor
    public static class DatabaseHealth {
        private String status;
        private String message;
    }
}