package com.onixbyte.deltaforceguide.service;

import com.onixbyte.deltaforceguide.domain.dto.ModificationResponse;
import com.onixbyte.deltaforceguide.domain.dto.PageResponse;
import com.onixbyte.deltaforceguide.repository.ModificationRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
public class ModificationQueryService {

    private final ModificationRepository modificationRepository;

    public ModificationQueryService(ModificationRepository modificationRepository) {
        this.modificationRepository = modificationRepository;
    }

    @Transactional(readOnly = true)
    public PageResponse<ModificationResponse> pageQuery(Pageable pageable) {
        return PageResponse.from(
                modificationRepository.findAllBy(pageable)
                        .map(ModificationResponse::from)
        );
    }

    @Transactional(readOnly = true)
    public ModificationResponse queryById(Long id) {
        return modificationRepository.findById(id)
                .map(ModificationResponse::from)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Modification not found: " + id));
    }
}

