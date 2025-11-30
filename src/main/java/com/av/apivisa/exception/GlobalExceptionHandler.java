package com.av.apivisa.exception;

import com.av.apivisa.dto.ErrorResponseTO;
import com.av.apivisa.dto.ValidationErrorResponseTO;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseTO> handleGenericException(Exception ex, HttpServletRequest request) {
        log.error("Internal error: {}", ex.getMessage(), ex);

        ErrorResponseTO error = new ErrorResponseTO(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "Internal server error",
                request.getRequestURI(),
                "INTERNAL_ERROR"
        );
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponseTO> handleIllegalArgumentException(IllegalArgumentException ex, HttpServletRequest request) {
        log.warn("Invalid request: {}", ex.getMessage());

        ErrorResponseTO error = new ErrorResponseTO(
                HttpStatus.BAD_REQUEST,
                ex.getMessage(),
                request.getRequestURI(),
                "INVALID_REQUEST"
        );
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<ErrorResponseTO> handleIllegalStateException(IllegalStateException ex, HttpServletRequest request) {
        log.warn("Invalid state: {}", ex.getMessage());

        ErrorResponseTO error = new ErrorResponseTO(
                HttpStatus.CONFLICT,
                ex.getMessage(),
                request.getRequestURI(),
                "INVALID_STATE"
        );
        return new ResponseEntity<>(error, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ErrorResponseTO> handleBadCredentialsException(BadCredentialsException ex, HttpServletRequest request) {
        log.warn("Invalid credentials: {}", ex.getMessage());

        ErrorResponseTO error = new ErrorResponseTO(
                HttpStatus.UNAUTHORIZED,
                "Invalid credentials",
                request.getRequestURI(),
                "INVALID_CREDENTIALS"
        );
        return new ResponseEntity<>(error, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ValidationErrorResponseTO> handleValidationExceptions(MethodArgumentNotValidException ex, HttpServletRequest request) {
        log.warn("Validating error: {}", ex.getMessage());

        ValidationErrorResponseTO errorResponse = new ValidationErrorResponseTO("Invalid input data");

        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errorResponse.addFieldError(fieldName, errorMessage);
        });

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<ErrorResponseTO> handleMaxSizeException(MaxUploadSizeExceededException ex, HttpServletRequest request) {
        log.warn("File too large: {}", ex.getMessage());

        ErrorResponseTO error = new ErrorResponseTO(
                HttpStatus.BAD_REQUEST,
                "File too large. Maximum size allowed: 10MB",
                request.getRequestURI(),
                "FILE_TOO_LARGE"
        );
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(SecurityException.class)
    public ResponseEntity<ErrorResponseTO> handleSecurityException(SecurityException ex, HttpServletRequest request) {
        log.warn("Security error: {}", ex.getMessage());

        ErrorResponseTO error = new ErrorResponseTO(
                HttpStatus.FORBIDDEN,
                ex.getMessage(),
                request.getRequestURI(),
                "SECURITY_ERROR"
        );
        return new ResponseEntity<>(error, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorResponseTO> handleBusinessException(BusinessException ex, HttpServletRequest request) {
        log.warn("Business error: {} - CODE: {}", ex.getMessage(), ex.getErrorCode());

        ErrorResponseTO error = new ErrorResponseTO(
                HttpStatus.CONFLICT, // 409 Conflict is more appropriate for resources that already exist.
                ex.getMessage(),
                request.getRequestURI(),
                ex.getErrorCode()
        );
        return new ResponseEntity<>(error, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(BatchProcessingException.class)
    public ResponseEntity<ErrorResponseTO> handleBatchProcessingException(BatchProcessingException ex, HttpServletRequest request) {
        log.warn("Error processing batch: {} - CODE {}", ex.getMessage(), ex.getErrorCode());

        ErrorResponseTO error = new ErrorResponseTO(
                HttpStatus.CONFLICT,
                ex.getMessage(),
                request.getRequestURI(),
                ex.getErrorCode()
        );
        return new ResponseEntity<>(error, HttpStatus.CONFLICT);
    }
}