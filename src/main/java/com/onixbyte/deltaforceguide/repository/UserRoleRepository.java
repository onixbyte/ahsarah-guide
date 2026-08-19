package com.onixbyte.deltaforceguide.repository;

import com.onixbyte.deltaforceguide.domain.entity.UserRole;
import com.onixbyte.deltaforceguide.domain.entity.UserRoleId;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data JPA repository for {@link UserRole} entity operations.
 *
 * @author zihluwang
 */
@Repository
public interface UserRoleRepository extends JpaRepository<UserRole, UserRoleId>, JpaSpecificationExecutor<UserRole> {

    /**
     * Find all roles assigned to a given user.
     *
     * @param userId the user ID
     * @return list of matching role assignments
     */
    @EntityGraph(attributePaths = {"user"})
    @Query("""
            select ur
            from UserRole ur
            where ur.user.id = :userId
            """)
    List<UserRole> findAllByUserId(@Param("userId") Long userId);

    /**
     * Check whether a user has a specific role.
     *
     * @param userId the user ID
     * @param role   the role name
     * @return true if the role is assigned, false otherwise
     */
    @Query("""
            select count(ur) > 0
            from UserRole ur
            where ur.user.id = :userId
              and ur.id.role = :role
            """)
    boolean existsByUserIdAndRole(@Param("userId") Long userId, @Param("role") String role);

    /**
     * Delete a specific role assignment for a user.
     *
     * @param userId the user ID
     * @param role   the role name
     */
    @Modifying
    @Query("""
            delete from UserRole ur
            where ur.user.id = :userId
              and ur.id.role = :role
            """)
    void deleteByUserIdAndRole(@Param("userId") Long userId, @Param("role") String role);

    /**
     * Delete all role assignments for a given user.
     *
     * @param userId the user ID
     */
    @Modifying
    @Query("""
            delete from UserRole ur
            where ur.user.id = :userId
            """)
    void deleteAllByUserId(@Param("userId") Long userId);
}
