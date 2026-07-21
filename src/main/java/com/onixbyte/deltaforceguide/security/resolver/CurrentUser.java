package com.onixbyte.deltaforceguide.security.resolver;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation for injecting the currently authenticated {@code User} into controller method
 * parameters. The resolver extracts the user from the {@code SecurityContext}.
 *
 * @author zihluwang
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface CurrentUser {}
