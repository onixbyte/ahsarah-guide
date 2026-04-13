package com.onixbyte.deltaforceguide.security.provider;

import com.onixbyte.deltaforceguide.domain.entity.UserCredential;
import com.onixbyte.deltaforceguide.exeption.BizException;
import com.onixbyte.deltaforceguide.manager.UserManager;
import com.onixbyte.deltaforceguide.repository.UserCredentialRepository;
import com.onixbyte.deltaforceguide.security.authentication.UsernamePasswordAuthentication;
import com.onixbyte.deltaforceguide.shared.CredentialProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class UsernamePasswordAuthenticationProvider implements AuthenticationProvider {

    private static final Logger log = LoggerFactory.getLogger(UsernamePasswordAuthenticationProvider.class);
    private final UserManager userManager;
    private final PasswordEncoder passwordEncoder;
    private final UserCredentialRepository userCredentialRepository;

    @Autowired
    public UsernamePasswordAuthenticationProvider(
            UserManager userManager,
            PasswordEncoder passwordEncoder,
            UserCredentialRepository userCredentialRepository
    ) {
        this.userManager = userManager;
        this.passwordEncoder = passwordEncoder;
        this.userCredentialRepository = userCredentialRepository;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        if (!(authentication instanceof UsernamePasswordAuthentication usernamePasswordAuthentication)) {
            throw new BizException(HttpStatus.INTERNAL_SERVER_ERROR, "用户认证失败，请稍后再试。");
        }

        // get userContainer from database
        var userContainer = userManager.findByUsername(usernamePasswordAuthentication.getPrincipal());
        if (userContainer.isEmpty()) {
            log.error("User {} is trying to authenticate but no userContainer found.", usernamePasswordAuthentication.getPrincipal());
            throw new BizException(HttpStatus.UNAUTHORIZED, "用户名或密码错误。");
        }

        var user = userContainer.get();

        var userCredentialExample = new UserCredential();
        userCredentialExample.setUserId(user.getId());
        userCredentialExample.setProvider(CredentialProvider.LOCAL);

        // get userContainer credentials from database
        var userCredentials = userCredentialRepository.findOne(Example.of(userCredentialExample))
                .orElseThrow(() -> new BizException(HttpStatus.UNAUTHORIZED, "您还没有配置密码，请联系管理员处理。"));

        // validate password
        if (!passwordEncoder.matches(usernamePasswordAuthentication.getCredentials(), userCredentials.getCredential())) {
            log.error("User {} is trying to authenticate but password is incorrect.", usernamePasswordAuthentication.getPrincipal());
            throw new BizException(HttpStatus.UNAUTHORIZED, "用户名或密码错误。");
        }

        // erase credentials
        usernamePasswordAuthentication.eraseCredentials();

        // set values
        usernamePasswordAuthentication.setAuthenticated(true);
        usernamePasswordAuthentication.setDetails(user);

        return usernamePasswordAuthentication;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthentication.class.isAssignableFrom(authentication);
    }
}

