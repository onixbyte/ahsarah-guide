package com.onixbyte.ahsarahguide.repository;

import com.onixbyte.ahsarahguide.domain.entity.Firearm;
import com.onixbyte.ahsarahguide.enumeration.FirearmType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data JPA repository for {@link Firearm} entity operations.
 *
 * @author zihluwang
 */
@Repository
public interface FirearmRepository extends JpaRepository<Firearm, Long>, JpaSpecificationExecutor<Firearm> {

	Page<Firearm> findAllByType(FirearmType type, Pageable pageable);

	List<Firearm> findByName(String name);
}

