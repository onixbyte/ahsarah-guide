package com.onixbyte.deltaforceguide.domain.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Entity representing an application user with authentication credentials.
 *
 * @author zihluwang
 */
@Entity
@Table(name = "app_user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "username", nullable = false)
    private String username;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "nickname")
    private String nickname;

    @Column(name = "avatar_url")
    private String avatarUrl;

    @Column(name = "enabled", nullable = false)
    private Boolean enabled = true;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserCredential> credentials = new ArrayList<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    @PreUpdate
    private void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    public List<UserCredential> getCredentials() {
        return credentials;
    }

    public void setCredentials(List<UserCredential> credentials) {
        this.credentials = credentials;
    }

    public void addCredential(UserCredential credential) {
        this.credentials.add(credential);
        credential.setUser(this);
    }

    public void removeCredential(UserCredential credential) {
        this.credentials.remove(credential);
        credential.setUser(null);
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private Long id;
        private String username;
        private String email;
        private String nickname;
        private String avatarUrl;
        private Boolean enabled;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;
        private List<UserCredential> credentials;

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder username(String username) {
            this.username = username;
            return this;
        }

        public Builder email(String email) {
            this.email = email;
            return this;
        }

        public Builder nickname(String nickname) {
            this.nickname = nickname;
            return this;
        }

        public Builder avatarUrl(String avatarUrl) {
            this.avatarUrl = avatarUrl;
            return this;
        }

        public Builder enabled(Boolean enabled) {
            this.enabled = enabled;
            return this;
        }

        public Builder createdAt(LocalDateTime createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public Builder updatedAt(LocalDateTime updatedAt) {
            this.updatedAt = updatedAt;
            return this;
        }

        public Builder credentials(List<UserCredential> credentials) {
            this.credentials = credentials;
            return this;
        }

        public User build() {
            User user = new User();
            user.id = this.id;
            user.username = this.username;
            user.email = this.email;
            user.nickname = this.nickname == null || this.nickname.isBlank()
                    ? this.username : this.nickname;
            user.avatarUrl = this.avatarUrl;
            user.enabled = this.enabled == null || this.enabled;
            user.createdAt = this.createdAt;
            user.updatedAt = this.updatedAt;
            user.credentials = this.credentials == null ? new ArrayList<>() : this.credentials;
            return user;
        }
    }
}
