package com.av.apivisa.service;

import com.av.apivisa.entity.ApiLog;
import com.av.apivisa.entity.User;
import com.av.apivisa.repository.ApiLogRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class LoggingService {

    private final ApiLogRepository apiLogRepository;

    public void logRequest(HttpServletRequest request, HttpServletResponse response,
                           long processingTimeMs, String requestBody, String responseBody) {
        try {
            // Get the current user directly from the SecurityContext.
            String userId = getCurrentUserId();

            ApiLog apiLog = new ApiLog();
            apiLog.setEndpoint(request.getRequestURI());
            apiLog.setHttpMethod(request.getMethod());
            apiLog.setStatusCode(response.getStatus());
            apiLog.setRequestBody(truncateIfNeeded(requestBody, 4000));
            apiLog.setResponseBody(truncateIfNeeded(responseBody, 4000));
            apiLog.setClientIp(getClientIp(request));
            apiLog.setUserAgent(request.getHeader("User-Agent"));
            apiLog.setProcessingTimeMs(processingTimeMs);
            apiLog.setUserId(userId);
            apiLog.setTimestamp(LocalDateTime.now());

            if (response.getStatus() >= 400) {
                apiLog.setErrorCode("HTTP_" + response.getStatus());
                apiLog.setErrorMessage("Error in request");
            }

            apiLogRepository.save(apiLog);

        } catch (Exception e) {
            System.err.println("Error logging: " + e.getMessage());
        }
    }

    private String getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof User) {
            User user = (User) authentication.getPrincipal();
            return user.getId();
        }
        return null;
    }

    private String getClientIp(HttpServletRequest request) {
        String xForwardedFor = request.getHeader("X-Forwarded-For");
        if (xForwardedFor != null && !xForwardedFor.isEmpty()) {
            return xForwardedFor.split(",")[0].trim();
        }
        return request.getRemoteAddr();
    }

    private String truncateIfNeeded(String text, int maxLength) {
        if (text == null) return null;
        if (text.length() <= maxLength) return text;
        return text.substring(0, maxLength - 3) + "...";
    }
}