package com.onixbyte.deltaforceguide.filter;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.onixbyte.deltaforceguide.client.TokenClient;
import com.onixbyte.deltaforceguide.exeption.BizException;
import com.onixbyte.deltaforceguide.manager.UserManager;
import com.onixbyte.deltaforceguide.security.authentication.UsernamePasswordAuthentication;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.jspecify.annotations.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.util.WebUtils;

import java.io.IOException;
import java.util.Objects;
import java.util.Optional;

@Component
public class TokenAuthenticationFilter extends OncePerRequestFilter {

    private final static Logger log = LoggerFactory.getLogger(TokenAuthenticationFilter.class);

    private final UserManager userManager;
    private final TokenClient tokenClient;
    private final HandlerExceptionResolver handlerExceptionResolver;

    public TokenAuthenticationFilter(
            UserManager userManager,
            TokenClient tokenClient,
            HandlerExceptionResolver handlerExceptionResolver
    ) {
        this.userManager = userManager;
        this.tokenClient = tokenClient;
        this.handlerExceptionResolver = handlerExceptionResolver;
    }

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
                throw new BizException(HttpStatus.UNAUTHORIZED, "登录已过期，请重新登录");
            }

            var user = userWrapper.get();
            var authentication = UsernamePasswordAuthentication.authenticated(user);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            filterChain.doFilter(request, response);
        } catch (JWTVerificationException e) {
            log.error("JWT verification failed.", e);
            handlerExceptionResolver.resolveException(request, response, null,
                    new BizException(HttpStatus.UNAUTHORIZED, "登录已过期，请重新登录"));
        } catch (BizException e) {
            handlerExceptionResolver.resolveException(request, response, null, e);
        }
    }
}

