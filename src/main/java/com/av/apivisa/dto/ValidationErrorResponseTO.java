package com.av.apivisa.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class ValidationErrorResponseTO {

    private String message;
    private List<FieldError> errors;

    public ValidationErrorResponseTO(String message) {
        this.message = message;
        this.errors = new ArrayList<>();
    }

    public void addFieldError(String field, String message) {
        this.errors.add(new FieldError(field, message));
    }

    @Data
    @AllArgsConstructor
    public static class FieldError {
        private String field;
        private String message;
    }
}