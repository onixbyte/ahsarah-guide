package com.onixbyte.deltaforceguide.repository;

import com.onixbyte.deltaforceguide.domain.entity.UserCredential;
import com.onixbyte.deltaforceguide.domain.entity.UserCredentialId;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data JPA repository for {@link UserCredential} entity operations.
 *
 * @author zihluwang
 */
@Repository
public interface UserCredentialRepository extends
        JpaRepository<UserCredential, UserCredentialId>,
        JpaSpecificationExecutor<UserCredential> {

    /**
     * Find all credentials belonging to a given user.
     *
     * @param userId the user ID
     * @return list of matching credentials
     */
    @EntityGraph(attributePaths = {"user"})
    @Query("""
            select uc
            from UserCredential uc
            where uc.user.id = :userId
            """)
    List<UserCredential> findAllByUserId(@Param("userId") Long userId);

    /**
     * Find a specific credential for a user by provider.
     *
     * @param userId   the user ID
     * @param provider the authentication provider identifier
     * @return an optional containing the matching credential, or empty if not found
     */
    @EntityGraph(attributePaths = {"user"})
    @Query("""
            select uc
            from UserCredential uc
            where uc.user.id = :userId
              and uc.id.provider = :provider
            """)
    Optional<UserCredential> findByUserIdAndProvider(@Param("userId") Long userId, @Param("provider") String provider);

    /**
     * Delete a specific credential for a user by provider.
     *
     * @param userId   the user ID
     * @param provider the authentication provider identifier
     */
    @Modifying
    @Query("""
            delete from UserCredential uc
            where uc.user.id = :userId
              and uc.id.provider = :provider
            """)
    void deleteByUserIdAndProvider(@Param("userId") Long userId, @Param("provider") String provider);

    /**
     * Delete all credentials for a given user.
     *
     * @param userId the user ID
     */
    @Modifying
    @Query("""
            delete from UserCredential uc
            where uc.user.id = :userId
            """)
    void deleteAllByUserId(@Param("userId") Long userId);
}



