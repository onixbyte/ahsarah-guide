package com.onixbyte.ahsarahguide.manager;

import com.onixbyte.ahsarahguide.domain.entity.UserRole;
import com.onixbyte.ahsarahguide.repository.UserRoleRepository;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Manager for user role persistence and authorisation data access.
 *
 * @author zihluwang
 */
@Component
public class UserRoleManager {

    private final UserRoleRepository userRoleRepository;

    public UserRoleManager(UserRoleRepository userRoleRepository) {
        this.userRoleRepository = userRoleRepository;
    }

    /**
     * Finds all role assignments for a specific user.
     *
     * @param userId the user ID
     * @return list of matching role assignments
     */
    public List<UserRole> findAllByUserId(Long userId) {
        return userRoleRepository.findAllByUserId(userId);
    }

    /**
     * Checks whether a user has a specific role.
     *
     * @param userId the user ID
     * @param role   the role name
     * @return true if the role is assigned, false otherwise
     */
    public boolean existsByUserIdAndRole(Long userId, String role) {
        return userRoleRepository.existsByUserIdAndRole(userId, role);
    }

    /**
     * Persists a new role assignment.
     *
     * @param userRole the role assignment to save
     * @return the saved role assignment
     */
    public UserRole save(UserRole userRole) {
        return userRoleRepository.save(userRole);
    }

    /**
     * Deletes a role assignment for a specific user and role.
     *
     * @param userId the user ID
     * @param role   the role name
     */
    public void deleteByUserIdAndRole(Long userId, String role) {
        userRoleRepository.deleteByUserIdAndRole(userId, role);
    }

    /**
     * Deletes all role assignments belonging to a user.
     *
     * @param userId the user ID
     */
    public void deleteAllByUserId(Long userId) {
        userRoleRepository.deleteAllByUserId(userId);
    }
}
