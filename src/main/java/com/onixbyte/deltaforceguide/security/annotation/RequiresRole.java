package com.onixbyte.deltaforceguide.security.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation for protecting controller methods or classes with role-based access control.
 * When applied, the current authenticated user must possess at least one of the specified roles.
 *
 * @author zihluwang
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface RequiresRole {

    /**
     * The required role names. The user must possess at least one of these.
     *
     * @return array of role name strings
     */
    String[] value();
}
