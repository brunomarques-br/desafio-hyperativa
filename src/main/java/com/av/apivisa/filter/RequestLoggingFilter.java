package com.av.apivisa.filter;

import com.av.apivisa.service.LoggingService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

@Component
@RequiredArgsConstructor
public class RequestLoggingFilter extends OncePerRequestFilter {

    private final LoggingService loggingService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        long startTime = System.currentTimeMillis();

        // Wrap request and response to capture content
        ContentCachingRequestWrapper wrappedRequest = new ContentCachingRequestWrapper(request, 1024 * 1024);
        ContentCachingResponseWrapper wrappedResponse = new ContentCachingResponseWrapper(response);

        try {
            filterChain.doFilter(wrappedRequest, wrappedResponse);
        } finally {
            long processingTime = System.currentTimeMillis() - startTime;

            // Get request and response body
            String requestBody = getRequestBody(wrappedRequest);
            String responseBody = getResponseBody(wrappedResponse);

            // Log the request
            loggingService.logRequest(request, response, processingTime, requestBody, responseBody);

            // Copy response body back to original response
            wrappedResponse.copyBodyToResponse();
        }
    }

    private String getRequestBody(ContentCachingRequestWrapper request) throws UnsupportedEncodingException {
        byte[] content = request.getContentAsByteArray();
        if (content.length > 0) {
            return new String(content, request.getCharacterEncoding());
        }
        return null;
    }

    private String getResponseBody(ContentCachingResponseWrapper response) throws UnsupportedEncodingException {
        byte[] content = response.getContentAsByteArray();
        if (content.length > 0) {
            return new String(content, response.getCharacterEncoding());
        }
        return null;
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getRequestURI();
        // Avoid logging health check endpoints to reduce noise logs.
        return path.startsWith("/api/health") || path.startsWith("/actuator");
    }
}
