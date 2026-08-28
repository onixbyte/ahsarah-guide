package com.onixbyte.ahsarahguide.security.resolver;

import io.swagger.v3.oas.annotations.Parameter;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation for injecting the currently authenticated {@code User} into controller method
 * parameters. The resolver extracts the user from the {@code SecurityContext}.
 * <p>
 * Marked hidden so it is not rendered as a request parameter in the OpenAPI documentation,
 * as the value is injected server-side rather than supplied by the client.
 *
 * @author zihluwang
 */
@Parameter(hidden = true)
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface CurrentUser {}
