package com.av.apivisa.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ApiLogResponseTO {
    private Long id;
    private String endpoint;
    private String httpMethod;
    private Integer statusCode;
    private String clientIp;
    private String userAgent;
    private Long processingTimeMs;
    private String userId;
    private LocalDateTime timestamp;
    private String errorCode;
    private String responseBody;
}