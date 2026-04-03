package com.onixbyte.deltaforceguide.repository;

import com.onixbyte.deltaforceguide.domain.entity.Modification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ModificationRepository extends JpaRepository<Modification, Long> {

    @EntityGraph(attributePaths = {"firearm"})
    Page<Modification> findAllBy(Pageable pageable);

    @Override
    @EntityGraph(attributePaths = {"firearm"})
    Optional<Modification> findById(Long id);
}


