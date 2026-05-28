package com.onixbyte.deltaforceguide.manager;

import com.onixbyte.deltaforceguide.domain.entity.UserCredential;
import com.onixbyte.deltaforceguide.repository.UserCredentialRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

/**
 * Manager for user credential persistence and authentication data access.
 *
 * @author zihluwang
 */
@Component
public class UserCredentialManager {

    private final UserCredentialRepository userCredentialRepository;

    public UserCredentialManager(UserCredentialRepository userCredentialRepository) {
        this.userCredentialRepository = userCredentialRepository;
    }

    /**
     * Finds all credentials belonging to a specific user.
     *
     * @param userId the user ID
     * @return list of matching credentials
     */
    public List<UserCredential> findAllByUserId(Long userId) {
        return userCredentialRepository.findAllByUserId(userId);
    }

    /**
     * Finds a credential for a specific user and provider combination.
     *
     * @param userId the user ID
     * @param provider the authentication provider
     * @return the matching credential, if found
     */
    public Optional<UserCredential> findByUserIdAndProvider(Long userId, String provider) {
        return userCredentialRepository.findByUserIdAndProvider(userId, provider);
    }

    /**
     * Persists a new or updated credential.
     *
     * @param userCredential the credential to save
     * @return the saved credential
     */
    public UserCredential save(UserCredential userCredential) {
        return userCredentialRepository.save(userCredential);
    }

    /**
     * Deletes a credential for a specific user and provider.
     *
     * @param userId the user ID
     * @param provider the authentication provider
     */
    public void deleteByUserIdAndProvider(Long userId, String provider) {
        userCredentialRepository.deleteByUserIdAndProvider(userId, provider);
    }

    /**
     * Deletes all credentials belonging to a user.
     *
     * @param userId the user ID
     */
    public void deleteAllByUserId(Long userId) {
        userCredentialRepository.deleteAllByUserId(userId);
    }
}



