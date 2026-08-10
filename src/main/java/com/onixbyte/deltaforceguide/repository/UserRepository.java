package com.onixbyte.deltaforceguide.repository;

import com.onixbyte.deltaforceguide.domain.entity.User;
import org.jspecify.annotations.NonNull;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Spring Data JPA repository for {@link User} entity operations.
 *
 * @author zihluwang
 */
@Repository
public interface UserRepository extends BaseRepository<User, Long> {

    @Override
    @EntityGraph(attributePaths = {"credentials"})
    @NonNull
    Optional<User> findById(@NonNull Long id);

    @EntityGraph(attributePaths = {"credentials"})
    Optional<User> findByUsername(String username);

    @EntityGraph(attributePaths = {"credentials"})
    Optional<User> findByEmail(String email);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    /**
     * Find a user by either username or email.
     *
     * @param principal the username or email to search for
     * @return an optional containing the matching user, or empty if not found
     */
    @EntityGraph(attributePaths = {"credentials"})
    @Query("""
            select u
            from User u
            where u.username = :principal
               or u.email = :principal
            """)
    Optional<User> findByUsernameOrEmail(@Param("principal") String principal);
}

