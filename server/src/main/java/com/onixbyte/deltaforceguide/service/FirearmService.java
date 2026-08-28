package com.onixbyte.deltaforceguide.service;

import com.onixbyte.deltaforceguide.domain.dto.FirearmRequest;
import com.onixbyte.deltaforceguide.domain.dto.FirearmResponse;
import com.onixbyte.deltaforceguide.domain.dto.PageResponse;
import com.onixbyte.deltaforceguide.domain.entity.Firearm;
import com.onixbyte.deltaforceguide.enumeration.FirearmType;
import com.onixbyte.deltaforceguide.exeption.NotFoundException;
import com.onixbyte.deltaforceguide.repository.FirearmRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

/**
 * Service handling firearm business logic including CRUD operations and queries.
 *
 * @author zihluwang
 */
@Service
public class FirearmService {

    private final FirearmRepository firearmRepository;

    public FirearmService(FirearmRepository firearmRepository) {
        this.firearmRepository = firearmRepository;
    }

    /**
     * Queries firearms with optional type filter and pagination.
     *
     * @param type optional firearm type filter
     * @param pageable pagination parameters
     * @return a paginated response of firearm records
     */
    public PageResponse<FirearmResponse> pageQuery(FirearmType type, Pageable pageable) {
        Page<Firearm> page = type == null
                ? firearmRepository.findAll(pageable)
                : firearmRepository.findAllByType(type, pageable);

        return PageResponse.from(page.map(FirearmResponse::from));
    }

    /**
     * Finds a firearm by its ID.
     *
     * @param id the firearm ID
     * @return the firearm response
     */
    public FirearmResponse queryById(Long id) {
        return firearmRepository.findById(id)
                .map(FirearmResponse::from)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Firearm not found: " + id));
    }

    /**
     * Creates a new firearm from the provided request data.
     *
     * @param request the firearm creation request
     * @return the created firearm response
     */
    public FirearmResponse addFirearm(FirearmRequest request) {
        var firearm = firearmRepository.save(Firearm.builder()
                .name(request.name())
                .type(request.type())
                .level(request.level())
                .calibre(request.calibre())
                .fireRate(request.fireRate())
                .armourDamage(request.armourDamage())
                .bodyDamage(request.bodyDamage())
                .review(request.review())
                .build());
        
        return FirearmResponse.from(firearm);
    }

    /**
     * Updates an existing firearm identified by ID.
     *
     * @param id the firearm ID
     * @param request the updated firearm data
     * @return the updated firearm response
     */
    public FirearmResponse updateFirearm(Long id, FirearmRequest request) {
        var firearm = firearmRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Firearm not found: " + id));

        firearm.setName(request.name());
        firearm.setType(request.type());
        firearm.setLevel(request.level());
        firearm.setCalibre(request.calibre());
        firearm.setFireRate(request.fireRate());
        firearm.setArmourDamage(request.armourDamage());
        firearm.setBodyDamage(request.bodyDamage());
        firearm.setReview(request.review());

        return FirearmResponse.from(firearmRepository.save(firearm));
    }

    /**
     * Deletes a firearm by its ID.
     *
     * @param id the firearm ID to delete
     */
    public void deleteFirearm(Long id) {
        Firearm firearm = firearmRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Firearm not found: " + id));
        firearmRepository.delete(firearm);
    }
}
