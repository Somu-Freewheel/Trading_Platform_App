package com.example.trading_app.config;

import com.example.trading_app.exception.RateLimitExceededException;
import com.example.trading_app.service.RateLimitService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
public class RateLimitInterceptor implements HandlerInterceptor {

    @Autowired
    private RateLimitService rateLimitService;

    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // Only apply rate limiting to signin endpoint
        if (!request.getRequestURI().contains("/auth/signin")) {
            return true;
        }

        // Get identifier - prefer email from request body, fallback to IP
        String identifier = extractIdentifier(request);

        // Check rate limit
        if (!rateLimitService.isAllowed(identifier)) {
            int remainingAttempts = rateLimitService.getRemainingAttempts(identifier);
            long timeRemaining = rateLimitService.getTimeRemainingInSeconds(identifier);

            // Send error response
            response.setStatus(429); // HTTP 429 - Too Many Requests
            response.setContentType("application/json");

            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("status", false);
            errorResponse.put("message", "Too many login attempts. Please try again later.");
            errorResponse.put("remainingAttempts", remainingAttempts);
            errorResponse.put("timeRemainingSeconds", timeRemaining);
            errorResponse.put("timeRemainingFormatted", formatTime(timeRemaining));

            response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
            return false;
        }

        return true;
    }

    /**
     * Extract identifier from request (email or IP)
     */
    private String extractIdentifier(HttpServletRequest request) throws IOException {
        // Try to get email from request body
        String email = extractEmailFromBody(request);

        if (email != null && !email.isEmpty()) {
            return email;
        }

        // Fallback to IP address
        return getClientIpAddress(request);
    }

    /**
     * Extract email from JSON request body
     */
    private String extractEmailFromBody(HttpServletRequest request) throws IOException {
        try {
            String body = new String(request.getInputStream().readAllBytes());
            if (body.isEmpty()) {
                return null;
            }

            Map<String, Object> jsonBody = objectMapper.readValue(body, Map.class);
            Object email = jsonBody.get("email");
            return email != null ? email.toString() : null;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Get client IP address from request
     */
    private String getClientIpAddress(HttpServletRequest request) {
        String xForwardedFor = request.getHeader("X-Forwarded-For");
        if (xForwardedFor != null && !xForwardedFor.isEmpty()) {
            return xForwardedFor.split(",")[0];
        }

        String xRealIp = request.getHeader("X-Real-IP");
        if (xRealIp != null && !xRealIp.isEmpty()) {
            return xRealIp;
        }

        return request.getRemoteAddr();
    }

    /**
     * Format time in seconds to readable format
     */
    private String formatTime(long seconds) {
        long minutes = seconds / 60;
        long secs = seconds % 60;
        return String.format("%d minutes %d seconds", minutes, secs);
    }
}


