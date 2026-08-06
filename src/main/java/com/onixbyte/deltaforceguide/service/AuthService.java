package com.onixbyte.deltaforceguide.service;

import com.onixbyte.deltaforceguide.domain.dto.LoginRequest;
import com.onixbyte.deltaforceguide.domain.entity.User;
import com.onixbyte.deltaforceguide.exeption.InternalServerErrorException;
import com.onixbyte.deltaforceguide.security.authentication.UsernamePasswordAuthentication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.stereotype.Service;

/**
 * Service handling user authentication, login, and session management.
 *
 * @author zihluwang
 */
@Service
public class AuthService {

    private static final Logger log = LoggerFactory.getLogger(AuthService.class);
    private final AuthenticationManager authenticationManager;

    public AuthService(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    /**
     * Authenticates a user with the given login credentials.
     * <p>
     * Delegates authentication to Spring Security's {@link AuthenticationManager} and verifies
     * that the result is of the expected {@link UsernamePasswordAuthentication} type.
     *
     * @param request the login credentials containing principle and password
     * @return the authenticated {@link User}
     */
    public User login(LoginRequest request) {
        var _authentication = authenticationManager.authenticate(UsernamePasswordAuthentication
                .unauthenticated(request.principle(), request.credential()));
        if (!(_authentication instanceof UsernamePasswordAuthentication authentication)) {
            log.error(
                    "Type mismatched, required type is UsernamePasswordAuthentication but got {}.",
                    _authentication.getClass()
            );
            throw new InternalServerErrorException("登录服务异常，请稍后再试。");
        }

        return authentication.getDetails();
    }

}
