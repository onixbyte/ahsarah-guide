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

	@Column(name = "credential", nullable = false, length = 255)
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
}
