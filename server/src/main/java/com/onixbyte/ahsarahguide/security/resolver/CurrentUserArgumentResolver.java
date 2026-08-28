package com.onixbyte.ahsarahguide.security.resolver;

import com.onixbyte.ahsarahguide.domain.entity.User;
import com.onixbyte.ahsarahguide.security.authentication.UsernamePasswordAuthentication;
import org.jspecify.annotations.NonNull;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.server.ResponseStatusException;

/**
 * Resolves {@link CurrentUser}-annotated controller method parameters by extracting the
 * {@link User} entity from the current {@link UsernamePasswordAuthentication} in the
 * security context.
 *
 * @author zihluwang
 */
@Component
public class CurrentUserArgumentResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(CurrentUser.class)
                && parameter.getParameterType().equals(User.class);
    }

    @Override
    public User resolveArgument(
            @NonNull MethodParameter parameter,
            ModelAndViewContainer mavContainer,
            @NonNull NativeWebRequest webRequest,
            WebDataBinderFactory binderFactory
    ) {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth instanceof UsernamePasswordAuthentication upa && upa.isAuthenticated()) {
            return upa.getDetails();
        }
        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "请先登录");
    }
}
