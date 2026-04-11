package com.onixbyte.deltaforceguide.service;

import com.onixbyte.deltaforceguide.domain.entity.User;
import com.onixbyte.deltaforceguide.domain.entity.UserCredential;
import com.onixbyte.deltaforceguide.manager.UserCredentialManager;
import com.onixbyte.deltaforceguide.manager.UserManager;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class UserService {

    private final UserManager userManager;
    private final UserCredentialManager userCredentialManager;

    public UserService(UserManager userManager, UserCredentialManager userCredentialManager) {
        this.userManager = userManager;
        this.userCredentialManager = userCredentialManager;
    }

    @Transactional(readOnly = true)
    public List<User> findAll() {
        return userManager.findAll();
    }

    @Transactional(readOnly = true)
    public User queryById(Long id) {
        return userManager.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found: " + id));
    }

    @Transactional(readOnly = true)
    public User queryByUsername(String username) {
        return userManager.findByUsername(username)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found: " + username));
    }

    @Transactional
    public User create(User user) {
        return userManager.save(user);
    }

    @Transactional
    public User update(User user) {
        if (user.getId() == null || userManager.findById(user.getId()).isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found: " + user.getId());
        }
        return userManager.save(user);
    }

    @Transactional(readOnly = true)
    public List<UserCredential> findCredentials(Long userId) {
        ensureUserExists(userId);
        return userCredentialManager.findAllByUserId(userId);
    }

    @Transactional(readOnly = true)
    public UserCredential queryCredential(Long userId, String provider) {
        ensureUserExists(userId);
        return userCredentialManager.findByUserIdAndProvider(userId, provider)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "User credential not found: userId=" + userId + ", provider=" + provider));
    }

    @Transactional
    public UserCredential upsertCredential(Long userId, String provider, String credential) {
        User user = ensureUserExists(userId);
        UserCredential userCredential = userCredentialManager.findByUserIdAndProvider(userId, provider)
                .orElseGet(UserCredential::new);
        userCredential.setUser(user);
        userCredential.setProvider(provider);
        userCredential.setCredential(credential);
        return userCredentialManager.save(userCredential);
    }

    @Transactional
    public void deleteCredential(Long userId, String provider) {
        ensureUserExists(userId);
        userCredentialManager.deleteByUserIdAndProvider(userId, provider);
    }

    @Transactional
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


