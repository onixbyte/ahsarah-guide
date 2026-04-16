package com.onixbyte.deltaforceguide.service;

import com.onixbyte.deltaforceguide.domain.dto.FirearmRequest;
import com.onixbyte.deltaforceguide.domain.dto.FirearmResponse;
import com.onixbyte.deltaforceguide.domain.dto.PageResponse;
import com.onixbyte.deltaforceguide.domain.entity.Firearm;
import com.onixbyte.deltaforceguide.enumeration.FirearmType;
import com.onixbyte.deltaforceguide.repository.FirearmRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
public class FirearmService {

    private final FirearmRepository firearmRepository;

    public FirearmService(FirearmRepository firearmRepository) {
        this.firearmRepository = firearmRepository;
    }

    @Transactional(readOnly = true)
    public PageResponse<FirearmResponse> pageQuery(FirearmType type, Pageable pageable) {
        Page<Firearm> page = type == null
                ? firearmRepository.findAll(pageable)
                : firearmRepository.findAllByType(type, pageable);

        return PageResponse.from(page.map(FirearmResponse::from));
    }

    @Transactional(readOnly = true)
    public FirearmResponse queryById(Long id) {
        return firearmRepository.findById(id)
                .map(FirearmResponse::from)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Firearm not found: " + id));
    }

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
}

