package com.onixbyte.deltaforceguide.manager;

import com.onixbyte.deltaforceguide.domain.entity.UserCredential;
import com.onixbyte.deltaforceguide.repository.UserCredentialRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Component
public class UserCredentialManager {

    private final UserCredentialRepository userCredentialRepository;

    public UserCredentialManager(UserCredentialRepository userCredentialRepository) {
        this.userCredentialRepository = userCredentialRepository;
    }

    @Transactional(readOnly = true)
    public List<UserCredential> findAllByUserId(Long userId) {
        return userCredentialRepository.findAllByUserId(userId);
    }

    @Transactional(readOnly = true)
    public Optional<UserCredential> findByUserIdAndProvider(Long userId, String provider) {
        return userCredentialRepository.findByUserIdAndProvider(userId, provider);
    }

    @Transactional
    public UserCredential save(UserCredential userCredential) {
        return userCredentialRepository.save(userCredential);
    }

    @Transactional
    public void deleteByUserIdAndProvider(Long userId, String provider) {
        userCredentialRepository.deleteByUserIdAndProvider(userId, provider);
    }

    @Transactional
    public void deleteAllByUserId(Long userId) {
        userCredentialRepository.deleteAllByUserId(userId);
    }
}



