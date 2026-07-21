package com.onixbyte.deltaforceguide.domain.entity;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.AttributeOverrides;
import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;

/**
 * Entity representing a role assignment linking a user to a security role.
 *
 * @author zihluwang
 */
@Entity
@Table(name = "user_role")
public class UserRole {

    @EmbeddedId
    @AttributeOverrides({
        @AttributeOverride(name = "userId", column = @Column(name = "user_id")),
        @AttributeOverride(name = "role", column = @Column(name = "role"))
    })
    private UserRoleId id = new UserRoleId();

    @MapsId("userId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false, foreignKey = @ForeignKey(name = "fk_user_role_user"))
    private User user;

    public UserRoleId getId() {
        return id;
    }

    public void setId(UserRoleId id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
        if (this.id == null) {
            this.id = new UserRoleId();
        }
        this.id.setUserId(user == null ? null : user.getId());
    }

    public Long getUserId() {
        return id == null ? null : id.getUserId();
    }

    public void setUserId(Long userId) {
        if (this.id == null) {
            this.id = new UserRoleId();
        }
        this.id.setUserId(userId);
    }

    public String getRole() {
        return id == null ? null : id.getRole();
    }

    public void setRole(String role) {
        if (this.id == null) {
            this.id = new UserRoleId();
        }
        this.id.setRole(role);
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private UserRoleId id;
        private User user;
        private Long userId;
        private String role;

        public Builder id(UserRoleId id) {
            this.id = id;
            return this;
        }

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
            userRole.id = this.id == null ? new UserRoleId() : this.id;
            userRole.user = this.user;
            if (this.user != null) {
                userRole.id.setUserId(this.user.getId());
            }
            if (this.userId != null) {
                userRole.id.setUserId(this.userId);
            }
            if (this.role != null) {
                userRole.id.setRole(this.role);
            }
            return userRole;
        }
    }
}
