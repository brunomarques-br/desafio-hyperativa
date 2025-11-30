package com.av.apivisa.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class CardResponseTO {

    private String id;
    private String batchNumber;
    private String lineIdentifier;
    private String numberingInBatch;
    private LocalDateTime createdAt;
    // I dont include cardNumber for security reasons
}