package com.onixbyte.deltaforceguide.service;

import com.onixbyte.deltaforceguide.domain.dto.FirearmResponse;
import com.onixbyte.deltaforceguide.domain.dto.PageResponse;
import com.onixbyte.deltaforceguide.repository.FirearmRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
public class FirearmQueryService {

    private final FirearmRepository firearmRepository;

    public FirearmQueryService(FirearmRepository firearmRepository) {
        this.firearmRepository = firearmRepository;
    }

    @Transactional(readOnly = true)
    public PageResponse<FirearmResponse> pageQuery(Pageable pageable) {
        return PageResponse.from(
                firearmRepository.findAll(pageable)
                        .map(FirearmResponse::from)
        );
    }

    @Transactional(readOnly = true)
    public FirearmResponse queryById(Long id) {
        return firearmRepository.findById(id)
                .map(FirearmResponse::from)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Firearm not found: " + id));
    }
}

