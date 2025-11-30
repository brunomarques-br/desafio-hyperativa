package com.av.apivisa.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class ErrorResponseTO {

    private LocalDateTime timestamp;
    private HttpStatus status;
    private String message;
    private String path;
    private String errorCode;

    public ErrorResponseTO(HttpStatus status, String message, String path) {
        this.timestamp = LocalDateTime.now();
        this.status = status;
        this.message = message;
        this.path = path;
    }

    public ErrorResponseTO(HttpStatus status, String message, String path, String errorCode) {
        this(status, message, path);
        this.errorCode = errorCode;
    }
}