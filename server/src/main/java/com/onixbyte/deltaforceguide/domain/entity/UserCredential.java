package com.onixbyte.deltaforceguide.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.AttributeOverride;
import jakarta.persistence.AttributeOverrides;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapsId;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

/**
 * Entity representing a user credential linked to an authentication provider.
 *
 * @author zihluwang
 */
@Entity
@Table(name = "app_user_credential")
public class UserCredential {

	@EmbeddedId
	@AttributeOverrides({
		@AttributeOverride(name = "userId", column = @Column(name = "user_id")),
		@AttributeOverride(name = "provider", column = @Column(name = "provider"))
	})
	private UserCredentialId id = new UserCredentialId();

	@MapsId("userId")
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "user_id", nullable = false, foreignKey = @ForeignKey(name = "fk_user_credential_user"))
	private User user;

	@Column(name = "credential", nullable = false)
	private String credential;

	public UserCredentialId getId() {
		return id;
	}

	public void setId(UserCredentialId id) {
		this.id = id;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
		if (this.id == null) {
			this.id = new UserCredentialId();
		}
		this.id.setUserId(user == null ? null : user.getId());
	}

	public Long getUserId() {
		return id == null ? null : id.getUserId();
	}

	public void setUserId(Long userId) {
		if (this.id == null) {
			this.id = new UserCredentialId();
		}
		this.id.setUserId(userId);
	}

	public String getProvider() {
		return id == null ? null : id.getProvider();
	}

	public void setProvider(String provider) {
		if (this.id == null) {
			this.id = new UserCredentialId();
		}
		this.id.setProvider(provider);
	}

	public String getCredential() {
		return credential;
	}

	public void setCredential(String credential) {
		this.credential = credential;
	}

	public static Builder builder() {
		return new Builder();
	}

	public static class Builder {

		private UserCredentialId id;
		private User user;
		private Long userId;
		private String provider;
		private String credential;

		public Builder id(UserCredentialId id) {
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

		public Builder provider(String provider) {
			this.provider = provider;
			return this;
		}

		public Builder credential(String credential) {
			this.credential = credential;
			return this;
		}

		public UserCredential build() {
			UserCredential userCredential = new UserCredential();
			userCredential.id = this.id == null ? new UserCredentialId() : this.id;
			userCredential.user = this.user;
			if (this.user != null) {
				userCredential.id.setUserId(this.user.getId());
			}
			if (this.userId != null) {
				userCredential.id.setUserId(this.userId);
			}
			if (this.provider != null) {
				userCredential.id.setProvider(this.provider);
			}
			userCredential.credential = this.credential;
			return userCredential;
		}
	}
}
