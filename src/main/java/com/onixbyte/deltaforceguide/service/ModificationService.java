package com.onixbyte.deltaforceguide.service;

import com.onixbyte.deltaforceguide.domain.dto.ModificationResponse;
import com.onixbyte.deltaforceguide.domain.dto.PageResponse;
import com.onixbyte.deltaforceguide.domain.entity.Modification;
import com.onixbyte.deltaforceguide.repository.ModificationRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
public class ModificationService {

    private final ModificationRepository modificationRepository;

    public ModificationService(ModificationRepository modificationRepository) {
        this.modificationRepository = modificationRepository;
    }

    @Transactional(readOnly = true)
    public PageResponse<ModificationResponse> pageQuery(Long firearmId, Pageable pageable) {
        Page<Modification> page = firearmId == null
                ? modificationRepository.findAllBy(pageable)
                : modificationRepository.findAllByFirearm_Id(firearmId, pageable);

        return PageResponse.from(page.map(ModificationResponse::from));
    }

    @Transactional(readOnly = true)
    public ModificationResponse queryById(Long id) {
        return modificationRepository.findById(id)
                .map(ModificationResponse::from)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Modification not found: " + id));
    }
}

