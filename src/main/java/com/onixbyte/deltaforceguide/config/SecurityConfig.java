package com.onixbyte.deltaforceguide.config;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.onixbyte.deltaforceguide.filter.TokenAuthenticationFilter;
import com.onixbyte.deltaforceguide.properties.CookieProperties;
import com.onixbyte.deltaforceguide.properties.TokenProperties;
import com.onixbyte.deltaforceguide.security.annotation.RequiresRole;
import com.onixbyte.deltaforceguide.security.authorisation.RoleBasedAuthorizationManager;
import com.onixbyte.deltaforceguide.security.provider.UsernamePasswordAuthenticationProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.Advisor;
import org.springframework.aop.support.annotation.AnnotationMatchingPointcut;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Role;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authorization.method.AuthorizationInterceptorsOrder;
import org.springframework.security.authorization.method.AuthorizationManagerBeforeMethodInterceptor;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import com.onixbyte.deltaforceguide.exeption.BizException;
import org.springframework.http.HttpStatus;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.ExceptionTranslationFilter;
import org.springframework.web.cors.CorsConfigurationSource;

/**
 * Spring Security configuration defining authentication, authorisation, and filter chains.
 *
 * @author zihluwang
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@EnableConfigurationProperties({TokenProperties.class, CookieProperties.class})
public class SecurityConfig {

    private static final Logger log = LoggerFactory.getLogger(SecurityConfig.class);

    /**
     * Configures the HTTP security filter chain including endpoint authorisation and JWT filter.
     *
     * @param http the HTTP security builder
     * @return the configured security filter chain
     */
    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity httpSecurity,
            CorsConfigurationSource corsConfigurationSource,
            TokenAuthenticationFilter tokenAuthenticationFilter
    ) throws Exception {
        return httpSecurity
                .cors((cors) -> cors
                        .configurationSource(corsConfigurationSource))
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement((customiser) -> customiser
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests((customiser) -> customiser
                        .anyRequest().permitAll()
                )
                .exceptionHandling(customiser -> customiser
                        .authenticationEntryPoint((request, response, authException) -> {
                            log.error("Unauthenticated request: {}", request, authException);
                            throw new BizException(HttpStatus.UNAUTHORIZED, "请先登录");
                        })
                        .accessDeniedHandler((request, response, accessDeniedException) -> {
                            log.error("Denied request: {}", request, accessDeniedException);
                            throw new BizException(HttpStatus.FORBIDDEN, "权限不足");
                        })
                )
                .addFilterAfter(tokenAuthenticationFilter, ExceptionTranslationFilter.class)
                .build();
    }

    /**
     * Provides the BCrypt password encoder for credential hashing.
     * @return the BCrypt password encoder
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Provides the authentication manager for the security configuration.
     *
     * @return the authentication manager
     */
    @Bean
    public AuthenticationManager authenticationManager(
            UsernamePasswordAuthenticationProvider usernamePasswordAuthenticationProvider
    ) {
        return new ProviderManager(
                usernamePasswordAuthenticationProvider
        );
    }

    @Bean
    public Algorithm algorithm(TokenProperties properties) {
        return Algorithm.HMAC256(properties.secret());
    }

    @Bean
    public JWTVerifier verifier(Algorithm algorithm, TokenProperties tokenProperties) {
        return JWT.require(algorithm)
                .withIssuer(tokenProperties.issuer())
                .build();
    }

    /**
     * Registers the {@link RequiresRole} annotation as a method-security interceptor.
     * Methods and classes annotated with {@code @RequiresRole} are checked by the
     * {@link RoleBasedAuthorizationManager}.
     *
     * @param manager the role-based authorisation manager
     * @return the advisor that intercepts {@code @RequiresRole}-annotated methods
     */
    @Bean
    @Role(BeanDefinition.ROLE_INFRASTRUCTURE)
    static Advisor requiresRoleAdvisor(RoleBasedAuthorizationManager manager) {
        var pointcut = new AnnotationMatchingPointcut(null, RequiresRole.class, true);
        var interceptor = new AuthorizationManagerBeforeMethodInterceptor(pointcut, manager);
        interceptor.setOrder(AuthorizationInterceptorsOrder.PRE_AUTHORIZE.getOrder() + 10);
        return interceptor;
    }
}
