package com.onixbyte.deltaforceguide.domain.entity;

import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.util.Objects;

/**
 * Composite key for the UserCredential entity, combining user ID and provider.
 *
 * @author zihluwang
 */
@Embeddable
public class UserCredentialId implements Serializable {

    private Long userId;

    private String provider;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        UserCredentialId that = (UserCredentialId) o;
        return Objects.equals(userId, that.userId) && Objects.equals(provider, that.provider);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, provider);
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private Long userId;
        private String provider;

        public Builder userId(Long userId) {
            this.userId = userId;
            return this;
        }

        public Builder provider(String provider) {
            this.provider = provider;
            return this;
        }

        public UserCredentialId build() {
            UserCredentialId id = new UserCredentialId();
            id.userId = this.userId;
            id.provider = this.provider;
            return id;
        }
    }
}


