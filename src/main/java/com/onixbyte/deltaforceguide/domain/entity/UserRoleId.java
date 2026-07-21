package com.onixbyte.deltaforceguide.domain.entity;

import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.util.Objects;

/**
 * Composite key for the UserRole entity, combining user ID and role name.
 *
 * @author zihluwang
 */
@Embeddable
public class UserRoleId implements Serializable {

    private Long userId;

    private String role;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        UserRoleId that = (UserRoleId) o;
        return Objects.equals(userId, that.userId) && Objects.equals(role, that.role);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, role);
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private Long userId;
        private String role;

        public Builder userId(Long userId) {
            this.userId = userId;
            return this;
        }

        public Builder role(String role) {
            this.role = role;
            return this;
        }

        public UserRoleId build() {
            UserRoleId id = new UserRoleId();
            id.userId = this.userId;
            id.role = this.role;
            return id;
        }
    }
}
