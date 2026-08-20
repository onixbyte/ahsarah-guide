package com.onixbyte.deltaforceguide.repository;

import com.onixbyte.deltaforceguide.domain.entity.Modification;
import org.jspecify.annotations.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data JPA repository for {@link Modification} entity operations,
 * including JSONB tag filtering for Postgres via the specification API.
 *
 * @author zihluwang
 */
@Repository
public interface ModificationRepository extends JpaRepository<Modification, Long>, JpaSpecificationExecutor<Modification> {

    @Override
    @EntityGraph(attributePaths = {"firearm"})
    @NonNull
    Page<Modification> findAll(Specification<Modification> spec, @NonNull Pageable pageable);

    @Override
    @EntityGraph(attributePaths = {"firearm"})
    @NonNull
    Optional<Modification> findById(@NonNull Long id);

    /**
     * Retrieve all distinct tag values from modifications, optionally filtered by firearm.
     *
     * @param firearmId optional firearm ID filter (nullable)
     * @return list of distinct tag strings
     */
    @Query(value = "SELECT DISTINCT jsonb_array_elements_text(cast(tags as jsonb)) FROM modification WHERE (:firearmId IS NULL OR firearm_id = :firearmId)", nativeQuery = true)
    List<String> findAllTags(@Param("firearmId") Long firearmId);
}
