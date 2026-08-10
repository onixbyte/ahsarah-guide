package com.onixbyte.deltaforceguide.security.provider;

import com.onixbyte.deltaforceguide.domain.entity.UserRole;
import com.onixbyte.deltaforceguide.exeption.InternalServerErrorException;
import com.onixbyte.deltaforceguide.exeption.UnauthorisedException;
import com.onixbyte.deltaforceguide.manager.UserManager;
import com.onixbyte.deltaforceguide.repository.UserCredentialRepository;
import com.onixbyte.deltaforceguide.repository.UserRoleRepository;
import com.onixbyte.deltaforceguide.security.authentication.UsernamePasswordAuthentication;
import com.onixbyte.deltaforceguide.shared.CredentialProvider;
import com.onixbyte.deltaforceguide.shared.Role;
import jakarta.persistence.criteria.Predicate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Authentication provider that validates username/password credentials against stored BCrypt hashes.
 *
 * @author zihluwang
 */
@Component
public class UsernamePasswordAuthenticationProvider implements AuthenticationProvider {

    private static final Logger log = LoggerFactory.getLogger(UsernamePasswordAuthenticationProvider.class);
    private final UserManager userManager;
    private final PasswordEncoder passwordEncoder;
    private final UserCredentialRepository userCredentialRepository;
    private final UserRoleRepository userRoleRepository;

    @Autowired
    public UsernamePasswordAuthenticationProvider(
            UserManager userManager,
            PasswordEncoder passwordEncoder,
            UserCredentialRepository userCredentialRepository,
            UserRoleRepository userRoleRepository) {
        this.userManager = userManager;
        this.passwordEncoder = passwordEncoder;
        this.userCredentialRepository = userCredentialRepository;
        this.userRoleRepository = userRoleRepository;
    }

    /**
     * Validates the username/password credentials against stored BCrypt hashes.
     *
     * @param authentication the authentication request object
     * @return a fully authenticated object including user details
     */
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        if (!(authentication instanceof UsernamePasswordAuthentication usernamePasswordAuthentication)) {
            throw new InternalServerErrorException("用户认证失败，请稍后再试。");
        }

        // get userContainer from database
        var userContainer = userManager.findByUsernameOrEmail(usernamePasswordAuthentication.getPrincipal());
        if (userContainer.isEmpty()) {
            log.error("User {} is trying to authenticate but no userContainer found.", usernamePasswordAuthentication.getPrincipal());
            throw new UnauthorisedException("用户名或密码错误。");
        }

        var user = userContainer.get();

        // get userContainer credentials from database
        var userCredentials = userCredentialRepository.findOne((root, query, cb) -> {
                    var predicates = new ArrayList<Predicate>();
                    predicates.add(cb.equal(root.get("provider"), CredentialProvider.LOCAL));
                    predicates.add(cb.equal(root.get("userId"), user.getId()));
                    return cb.and(predicates.toArray(Predicate[]::new));
                })
                .orElseThrow(() -> new UnauthorisedException("您还没有配置密码，请联系管理员处理。"));

        // validate password
        if (!passwordEncoder.matches(usernamePasswordAuthentication.getCredentials(), userCredentials.getCredential())) {
            log.error("User {} is trying to authenticate but password is incorrect.", usernamePasswordAuthentication.getPrincipal());
            throw new UnauthorisedException("用户名或密码错误。");
        }

        var role = userRoleRepository.findOne((root, query, cb) -> cb.equal(root.get("userId"), user.getId()))
                .orElseGet(() -> {
                    var ur = new UserRole();
                    ur.setUserId(user.getId());
                    ur.setRole(Role.ROLE_USER);
                    return ur;
                });

        // erase credentials
        usernamePasswordAuthentication.eraseCredentials();

        // set values
        usernamePasswordAuthentication.setAuthenticated(true);
        usernamePasswordAuthentication.setDetails(user);
        usernamePasswordAuthentication.setAuthorities(List.of(new SimpleGrantedAuthority(role.getRole())));

        return usernamePasswordAuthentication;
    }

    /**
     * Checks if this provider supports the given authentication type.
     *
     * @param authentication the authentication class to check
     * @return true if this provider supports the given type
     */
    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthentication.class.isAssignableFrom(authentication);
    }
}

