package com.av.apivisa.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class FileUploadResponseTO {

    private String batchId;
    private String batchNumber;
    private Integer totalRecords;
    private Integer processedRecords;
    private Integer successCount;
    private Integer errorCount;
    private List<String> errors;
    private List<CardResponseTO> savedCards;
    private String status;
    private String message;

    public FileUploadResponseTO(String batchId, String batchNumber, Integer totalRecords, Integer successCount,
                                Integer errorCount, List<String> errors, String status, String message) {
        this.batchId = batchId;
        this.batchNumber = batchNumber;
        this.totalRecords = totalRecords;
        this.successCount = successCount;
        this.errorCount = errorCount;
        this.errors = errors;
        this.status = status;
        this.message = message;
        this.processedRecords = successCount + errorCount;
    }
}