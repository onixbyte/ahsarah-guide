package com.onixbyte.deltaforceguide.repository;

import com.onixbyte.deltaforceguide.domain.entity.Firearm;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FirearmRepository extends JpaRepository<Firearm, Long> {
}

