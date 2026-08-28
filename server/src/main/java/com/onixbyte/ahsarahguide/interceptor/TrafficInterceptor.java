package com.onixbyte.ahsarahguide.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.jspecify.annotations.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class TrafficInterceptor implements HandlerInterceptor {

    private static final Logger log = LoggerFactory.getLogger(TrafficInterceptor.class);

    @Override
    public boolean preHandle(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull Object handler
    ) {
        var ip = resolveClientIp(request);
        var method = request.getMethod();
        var uri = request.getRequestURI();
        var query = request.getQueryString();
        var contentType = request.getContentType();
        var contentLength = request.getContentLength();
        var userAgent = request.getHeader("User-Agent");

        log.debug("Request method={}, uri={}, query={}, ip={}, content-type={}, content-length={}, user-agent={}",
                method, uri, query, ip, contentType, contentLength, userAgent);
        return true;
    }

    private String resolveClientIp(HttpServletRequest request) {
        var xForwardedFor = request.getHeader("X-Forwarded-For");
        if (xForwardedFor != null && !xForwardedFor.isBlank()) {
            return xForwardedFor.split(",")[0].trim();
        }
        var xRealIp = request.getHeader("X-Real-IP");
        if (xRealIp != null && !xRealIp.isBlank()) {
            return xRealIp.trim();
        }
        return request.getRemoteAddr();
    }
}
