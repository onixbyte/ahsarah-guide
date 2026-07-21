package com.onixbyte.deltaforceguide.security.authorisation;

import com.onixbyte.deltaforceguide.security.annotation.RequiresRole;
import org.aopalliance.intercept.MethodInvocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.function.Supplier;

/**
 * Spring Security {@link AuthorizationManager} that enforces {@link RequiresRole} annotations.
 * Checks that the authenticated user possesses at least one of the required roles.
 *
 * @author zihluwang
 */
@Component
public class RoleBasedAuthorizationManager implements AuthorizationManager<MethodInvocation> {

    private static final Logger log = LoggerFactory.getLogger(RoleBasedAuthorizationManager.class);

    @Override
    public AuthorizationDecision check(
            Supplier<Authentication> authenticationSupplier,
            MethodInvocation invocation
    ) {
        var auth = authenticationSupplier.get();
        if (auth == null || !auth.isAuthenticated()) {
            log.debug("Access denied: no authenticated user");
            return new AuthorizationDecision(false);
        }

        var annotation = invocation.getMethod().getAnnotation(RequiresRole.class);
        if (annotation == null) {
            annotation = invocation.getMethod().getDeclaringClass()
                    .getAnnotation(RequiresRole.class);
        }
        if (annotation == null) {
            return new AuthorizationDecision(true);
        }

        var requiredRoles = Set.of(annotation.value());
        var hasRole = auth.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .anyMatch(requiredRoles::contains);

        if (!hasRole) {
            log.debug("Access denied: user lacks required roles {}", requiredRoles);
        }
        return new AuthorizationDecision(hasRole);
    }
}
