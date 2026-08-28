package com.onixbyte.deltaforceguide.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;

/**
 * Entity representing a role assignment linking a user to a security role.
 * <p>
 * A user owns at most one role assignment, so the user ID is the primary key.
 *
 * @author zihluwang
 */
@Entity
@Table(name = "user_role")
public class UserRole {

    @Id
    @Column(name = "user_id")
    private Long userId;

    @MapsId("userId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false, foreignKey = @ForeignKey(name = "fk_user_role_user"))
    private User user;

    @Column(name = "role", nullable = false, length = 32)
    private String role;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
        if (user != null) {
            this.userId = user.getId();
        }
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private User user;
        private Long userId;
        private String role;

        public Builder user(User user) {
            this.user = user;
            return this;
        }

        public Builder userId(Long userId) {
            this.userId = userId;
            return this;
        }

        public Builder role(String role) {
            this.role = role;
            return this;
        }

        public UserRole build() {
            UserRole userRole = new UserRole();
            userRole.user = this.user;
            userRole.role = this.role;
            if (this.user != null) {
                userRole.userId = this.user.getId();
            }
            if (this.userId != null) {
                userRole.userId = this.userId;
            }
            return userRole;
        }
    }
}
