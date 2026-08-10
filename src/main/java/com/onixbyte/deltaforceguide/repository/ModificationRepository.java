package com.onixbyte.deltaforceguide.repository;

import com.onixbyte.deltaforceguide.domain.entity.Modification;
import org.jspecify.annotations.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data JPA repository for {@link Modification} entity operations,
 * including native JSONB tag filtering for Postgres.
 *
 * @author zihluwang
 */
@Repository
public interface ModificationRepository extends BaseRepository<Modification, Long> {

    @EntityGraph(attributePaths = {"firearm"})
    Page<Modification> findAllBy(Pageable pageable);

    @EntityGraph(attributePaths = {"firearm"})
    Page<Modification> findAllByFirearm_Id(Long firearmId, Pageable pageable);

    @Override
    @EntityGraph(attributePaths = {"firearm"})
    @NonNull
    Optional<Modification> findById(@NonNull Long id);

    /**
     * Page query modifications with optional firearm and JSONB tag filtering.
     *
     * @param firearmId optional firearm ID filter (nullable)
     * @param tagsJson  optional JSON array of tags to match via Postgres {@code @>} operator (nullable)
     * @param pageable  pagination information
     * @return a page of matching modifications
     */
    @Query(value = """
            SELECT * FROM modification m
            WHERE (:firearmId IS NULL OR m.firearm_id = :firearmId)
              AND (CAST(:tagsJson AS text) IS NULL OR cast(m.tags as jsonb) @> cast(CAST(:tagsJson AS text) as jsonb))
            """,
            countQuery = """
            SELECT count(*) FROM modification m
            WHERE (:firearmId IS NULL OR m.firearm_id = :firearmId)
              AND (CAST(:tagsJson AS text) IS NULL OR cast(m.tags as jsonb) @> cast(CAST(:tagsJson AS text) as jsonb))
            """,
            nativeQuery = true)
    Page<Modification> pageQueryByFirearmAndTags(@Param("firearmId") Long firearmId, @Param("tagsJson") String tagsJson, Pageable pageable);

    /**
     * Retrieve all distinct tag values from modifications, optionally filtered by firearm.
     *
     * @param firearmId optional firearm ID filter (nullable)
     * @return list of distinct tag strings
     */
    @Query(value = "SELECT DISTINCT jsonb_array_elements_text(cast(tags as jsonb)) FROM modification WHERE (:firearmId IS NULL OR firearm_id = :firearmId)", nativeQuery = true)
    List<String> findAllTags(@Param("firearmId") Long firearmId);
}
