package com.av.apivisa.exception;

public class BatchProcessingException extends BusinessException {

    public BatchProcessingException(String message) {
        super(message, "BATCH_PROCESSING_ERROR");
    }

    public BatchProcessingException(String message, Throwable cause) {
        super(message, cause);
    }
}