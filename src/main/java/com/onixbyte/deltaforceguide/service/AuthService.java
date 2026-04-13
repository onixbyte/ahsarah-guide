package com.onixbyte.deltaforceguide.service;

import com.onixbyte.deltaforceguide.domain.dto.LoginRequest;
import com.onixbyte.deltaforceguide.domain.entity.User;
import com.onixbyte.deltaforceguide.exeption.BizException;
import com.onixbyte.deltaforceguide.security.authentication.UsernamePasswordAuthentication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

@Service
public class AuthService {

    private static final Logger log = LoggerFactory.getLogger(AuthService.class);
    private final AuthenticationManager authenticationManager;

    public AuthService(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    public User login(LoginRequest request) {
        var _authentication = authenticationManager.authenticate(UsernamePasswordAuthentication
                .unauthenticated(request.principle(), request.credential()));
        if (!(_authentication instanceof UsernamePasswordAuthentication authentication)) {
            log.error(
                    "Type mismatched, required type is UsernamePasswordAuthentication but got {}.",
                    _authentication.getClass()
            );
            throw new BizException(HttpStatus.INTERNAL_SERVER_ERROR, "登录服务异常，请稍后再试。");
        }

        return authentication.getDetails();
    }

}
