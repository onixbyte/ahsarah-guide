package com.onixbyte.ahsarahguide.filter;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.onixbyte.ahsarahguide.client.TokenClient;
import com.onixbyte.ahsarahguide.exeption.UnauthorisedException;
import com.onixbyte.ahsarahguide.manager.CookieManager;
import com.onixbyte.ahsarahguide.manager.UserManager;
import com.onixbyte.ahsarahguide.manager.UserRoleManager;
import com.onixbyte.ahsarahguide.security.authentication.UsernamePasswordAuthentication;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.jspecify.annotations.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.util.WebUtils;

import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Servlet filter that extracts and validates JWT tokens from httpOnly cookies for each request.
 *
 * @author zihluwang
 */
@Component
public class TokenAuthenticationFilter extends OncePerRequestFilter {

    private final static Logger log = LoggerFactory.getLogger(TokenAuthenticationFilter.class);
    private static final Duration ACCESS_TOKEN_RENEW_THRESHOLD = Duration.ofMinutes(5);

    private final UserManager userManager;
    private final UserRoleManager userRoleManager;
    private final TokenClient tokenClient;
    private final CookieManager cookieManager;
    private final HandlerExceptionResolver handlerExceptionResolver;

    public TokenAuthenticationFilter(
            UserManager userManager,
            UserRoleManager userRoleManager,
            TokenClient tokenClient,
            CookieManager cookieManager,
            HandlerExceptionResolver handlerExceptionResolver
    ) {
        this.userManager = userManager;
        this.userRoleManager = userRoleManager;
        this.tokenClient = tokenClient;
        this.cookieManager = cookieManager;
        this.handlerExceptionResolver = handlerExceptionResolver;
    }

    /**
     * Extracts JWT from httpOnly cookie, validates it, and sets the security context.
     *
     * @param request the HTTP request
     * @param response the HTTP response
     * @param filterChain the filter chain
     */
    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
        var token = Optional.ofNullable(WebUtils.getCookie(request, "AccessToken"))
                .map(Cookie::getValue)
                .orElse(null);
        if (Objects.isNull(token) || token.isBlank()) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            var decodedToken = tokenClient.verifyToken(token);
            var username = decodedToken.getSubject();

            var userWrapper = userManager.findByUsername(username);
            if (userWrapper.isEmpty()) {
                throw new UnauthorisedException("登录已过期，请重新登录");
            }

            var user = userWrapper.get();
            var roles = userRoleManager.findAllByUserId(user.getId());
            var authorities = roles.stream()
                    .map(r -> new SimpleGrantedAuthority(r.getRole()))
                    .collect(Collectors.toList());
            var authentication = UsernamePasswordAuthentication.authenticated(user, authorities);
            SecurityContextHolder.getContext().setAuthentication(authentication);

            if (shouldRenew(decodedToken.getExpiresAt().toInstant())) {
                var renewedToken = tokenClient.generateToken(user);
                var renewedTokenCookie = cookieManager.buildCookie("AccessToken", renewedToken);
                response.addHeader(HttpHeaders.SET_COOKIE, renewedTokenCookie.toString());
            }

            filterChain.doFilter(request, response);
        } catch (JWTVerificationException e) {
            log.error("JWT verification failed.", e);
            handlerExceptionResolver.resolveException(request, response, null,
                    new UnauthorisedException("登录已过期，请重新登录"));
        } catch (ResponseStatusException e) {
            handlerExceptionResolver.resolveException(request, response, null, e);
        }
    }

    private boolean shouldRenew(Instant expiresAt) {
        return Duration.between(Instant.now(), expiresAt).compareTo(ACCESS_TOKEN_RENEW_THRESHOLD) < 0;
    }
}

