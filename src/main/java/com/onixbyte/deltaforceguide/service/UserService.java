package com.onixbyte.deltaforceguide.service;

import com.onixbyte.deltaforceguide.domain.entity.User;
import com.onixbyte.deltaforceguide.domain.entity.UserCredential;
import com.onixbyte.deltaforceguide.manager.UserCredentialManager;
import com.onixbyte.deltaforceguide.manager.UserManager;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

/**
 * Service for user account management and profile operations.
 *
 * @author zihluwang
 */
@Service
public class UserService {

    private final UserManager userManager;
    private final UserCredentialManager userCredentialManager;

    public UserService(UserManager userManager, UserCredentialManager userCredentialManager) {
        this.userManager = userManager;
        this.userCredentialManager = userCredentialManager;
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
}


