package com.onixbyte.deltaforceguide.service;

import com.onixbyte.deltaforceguide.domain.dto.*;
import com.onixbyte.deltaforceguide.domain.entity.User;
import com.onixbyte.deltaforceguide.domain.entity.UserCredential;
import com.onixbyte.deltaforceguide.domain.entity.UserRole;
import com.onixbyte.deltaforceguide.exeption.BadRequestException;
import com.onixbyte.deltaforceguide.manager.UserCredentialManager;
import com.onixbyte.deltaforceguide.manager.UserManager;
import com.onixbyte.deltaforceguide.manager.UserRoleManager;
import com.onixbyte.deltaforceguide.shared.CredentialProvider;
import com.onixbyte.deltaforceguide.shared.Role;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Objects;

/**
 * Service for user account management and profile operations.
 *
 * @author zihluwang
 */
@Service
public class UserService {

    private final UserManager userManager;
    private final UserCredentialManager userCredentialManager;
    private final UserRoleManager userRoleManager;
    private final PasswordEncoder passwordEncoder;

    public UserService(
            UserManager userManager,
            UserCredentialManager userCredentialManager,
            UserRoleManager userRoleManager,
            PasswordEncoder passwordEncoder
    ) {
        this.userManager = userManager;
        this.userCredentialManager = userCredentialManager;
        this.userRoleManager = userRoleManager;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Retrieves all registered users.
     *
     * @return list of all users
     */
    public List<User> findAll() {
        return userManager.findAll();
    }

    /**
     * Finds a user by their ID.
     *
     * @param id the user ID
     * @return the user
     */
    public User queryById(Long id) {
        return userManager.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found: " + id));
    }

    /**
     * Finds a user by their username.
     *
     * @param username the username to search for
     * @return the user
     */
    public User queryByUsername(String username) {
        return userManager.findByUsername(username)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found: " + username));
    }

    /**
     * Creates a new user account.
     *
     * @param user the user entity to persist
     * @return the saved user entity
     */
    public User create(User user) {
        return userManager.save(user);
    }

    /**
     * Updates an existing user account.
     *
     * @param user the user entity with updated fields
     * @return the saved user entity
     */
    public User update(User user) {
        if (user.getId() == null || userManager.findById(user.getId()).isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found: " + user.getId());
        }
        return userManager.save(user);
    }

    /**
     * Finds all credentials associated with a user.
     *
     * @param userId the user ID
     * @return list of user credentials
     */
    public List<UserCredential> findCredentials(Long userId) {
        ensureUserExists(userId);
        return userCredentialManager.findAllByUserId(userId);
    }

    /**
     * Queries a specific credential for a user by provider.
     *
     * @param userId   the user ID
     * @param provider the authentication provider
     * @return the matching credential
     */
    public UserCredential queryCredential(Long userId, String provider) {
        ensureUserExists(userId);
        return userCredentialManager.findByUserIdAndProvider(userId, provider)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "User credential not found: userId=" + userId + ", provider=" + provider));
    }

    /**
     * Creates or updates a credential for a user and provider.
     *
     * @param userId     the user ID
     * @param provider   the authentication provider
     * @param credential the credential value
     * @return the saved credential
     */
    public UserCredential upsertCredential(Long userId, String provider, String credential) {
        User user = ensureUserExists(userId);
        UserCredential userCredential = userCredentialManager.findByUserIdAndProvider(userId, provider)
                .orElseGet(UserCredential::new);
        userCredential.setUser(user);
        userCredential.setProvider(provider);
        userCredential.setCredential(credential);
        return userCredentialManager.save(userCredential);
    }

    /**
     * Deletes a specific credential for a user by provider.
     *
     * @param userId   the user ID
     * @param provider the authentication provider
     */
    public void deleteCredential(Long userId, String provider) {
        ensureUserExists(userId);
        userCredentialManager.deleteByUserIdAndProvider(userId, provider);
    }

    /**
     * Deletes a user and all associated credentials.
     *
     * @param id the user ID to delete
     */
    public void deleteById(Long id) {
        ensureUserExists(id);
        userCredentialManager.deleteAllByUserId(id);
        userManager.deleteById(id);
    }

    private User ensureUserExists(Long userId) {
        return userManager.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found: " + userId));
    }

    /**
     * Builds the full profile response for the given user, including their assigned roles.
     *
     * @param user the authenticated user
     * @return the user's profile response
     */
    public UserProfileResponse getProfile(User user) {
        var roles = userRoleManager.findAllByUserId(user.getId()).stream()
                .map(UserRole::getRole)
                .toList();
        return UserProfileResponse.from(user, roles);
    }

    /**
     * Updates the current user's profile information (nickname and email).
     * If the email address changes, the email verification status is reset.
     *
     * @param user    the authenticated user
     * @param request the update request containing new nickname and email
     * @return the updated user profile response
     */
    public UserProfileResponse updateProfile(User user, UpdateProfileRequest request) {
        if (StringUtils.hasText(request.nickname())) {
            user.setNickname(request.nickname());
        }

        userManager.save(user);
        return getProfile(user);
    }

    /**
     * Changes the current user's password after validating the old password.
     *
     * @param user    the authenticated user
     * @param request the change password request containing old and new passwords
     */
    public void changePassword(User user, ChangePasswordRequest request) {
        var credential = userCredentialManager.findByUserIdAndProvider(user.getId(), CredentialProvider.LOCAL);
        if (credential.isEmpty() || !passwordEncoder.matches(request.oldPassword(), credential.get().getCredential())) {
            throw new BadRequestException("旧密码不正确。");
        }
        var encoded = passwordEncoder.encode(request.newPassword());
        upsertCredential(user.getId(), CredentialProvider.LOCAL, encoded);
    }

    /**
     * Retrieves a paginated list of the current user's custom modification builds.
     * This is a skeleton implementation that returns an empty page.
     * It will be activated when the CustomModification entity is introduced.
     *
     * @param user     the authenticated user
     * @param pageable pagination parameters
     * @return an empty paginated response
     */
    public PageResponse<BuildSummaryResponse> getBuilds(User user, Pageable pageable) {
        var emptyPage = Page.<BuildSummaryResponse>empty(pageable);
        return PageResponse.from(emptyPage);
    }

    /**
     * Assigns a role to a user. Only {@code ROLE_ADMIN} may be assigned via this API.
     *
     * @param userId the target user ID
     * @param role   the role to assign
     */
    public void assignRole(Long userId, String role) {
        if (!Role.ROLE_ADMIN.equals(role)) {
            throw new BadRequestException("只能通过API分配ROLE_ADMIN角色。");
        }
        var user = ensureUserExists(userId);
        if (userRoleManager.existsByUserIdAndRole(userId, role)) {
            return; // already assigned — idempotent
        }
        var userRole = UserRole.builder()
                .user(user)
                .role(role)
                .build();
        userRoleManager.save(userRole);
    }

    /**
     * Removes a role from a user. Only {@code ROLE_ADMIN} may be removed via this API.
     *
     * @param userId the target user ID
     * @param role   the role to remove
     */
    public void removeRole(Long userId, String role) {
        if (!Role.ROLE_ADMIN.equals(role)) {
            throw new BadRequestException("只能通过API移除ROLE_ADMIN角色。");
        }
        ensureUserExists(userId);
        userRoleManager.deleteByUserIdAndRole(userId, role);
    }
}


