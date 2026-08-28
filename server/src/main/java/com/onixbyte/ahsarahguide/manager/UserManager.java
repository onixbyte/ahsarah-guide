package com.onixbyte.ahsarahguide.manager;

import com.onixbyte.ahsarahguide.domain.entity.User;
import com.onixbyte.ahsarahguide.domain.entity.UserRole;
import com.onixbyte.ahsarahguide.repository.UserRepository;
import com.onixbyte.ahsarahguide.repository.UserRoleRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Manager for user entity persistence and query operations.
 *
 * @author zihluwang
 */
@Component
public class UserManager {

    private final UserRepository userRepository;
    private final UserRoleRepository userRoleRepository;

    public UserManager(UserRepository userRepository, UserRoleRepository userRoleRepository) {
        this.userRepository = userRepository;
        this.userRoleRepository = userRoleRepository;
    }

    /**
     * Finds a user by their ID.
     *
     * @param id the user ID
     * @return the matching user, if found
     */
    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    /**
     * Retrieves all registered users.
     * @return list of all users
     */
    public List<User> findAll() {
        return userRepository.findAll();
    }

    /**
     * Finds a user by their username.
     *
     * @param username the username to search for
     * @return the matching user, if found
     */
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Transactional(readOnly = true)
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    /**
     * Persists a new or updated user.
     *
     * @param user the user to save
     * @return the saved user
     */
    public User save(User user) {
        return userRepository.save(user);
    }

    /**
     * Deletes a user by their ID.
     *
     * @param id the user ID to delete
     */
    public void deleteById(Long id) {
        userRepository.deleteById(id);
    }

    /**
     * Finds a user by their username or email address.
     *
     * @param principal the username or email to search for
     * @return the matching user, if found
     */
    public Optional<User> findByUsernameOrEmail(String principal) {
        return userRepository.findByUsernameOrEmail(principal);
    }

    /**
     * Checks whether a username is already registered.
     *
     * @param username the username to check
     * @return true if the username is already taken
     */
    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    /**
     * Checks whether an email address is already registered.
     *
     * @param email the email address to check
     * @return true if the email address is already taken
     */
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    /**
     * Persists a new user together with an initial role assignment in a single transaction.
     *
     * @param user the user to persist, whose credentials are cascaded
     * @param role the initial role name to assign
     * @return the saved user
     */
    @Transactional
    public User createWithRole(User user, String role) {
        var saved = userRepository.save(user);
        userRoleRepository.save(UserRole.builder()
                .user(saved)
                .role(role)
                .build());
        return saved;
    }
}

