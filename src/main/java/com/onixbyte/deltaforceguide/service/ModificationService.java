package com.onixbyte.deltaforceguide.service;

import com.onixbyte.deltaforceguide.domain.dto.ModificationResponse;
import com.onixbyte.deltaforceguide.domain.dto.PageResponse;
import com.onixbyte.deltaforceguide.domain.entity.Modification;
import com.onixbyte.deltaforceguide.repository.ModificationRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class ModificationService {

    private final ModificationRepository modificationRepository;
    private final ObjectMapper objectMapper;

    public ModificationService(ModificationRepository modificationRepository, ObjectMapper objectMapper) {
        this.modificationRepository = modificationRepository;
        this.objectMapper = objectMapper;
    }

    @Transactional(readOnly = true)
    public PageResponse<ModificationResponse> pageQuery(Long firearmId, List<String> tags, Pageable pageable) {
        String tagsJson = null;
        if (tags != null && !tags.isEmpty()) {
            try {
                tagsJson = objectMapper.writeValueAsString(tags);
            } catch (JsonProcessingException e) {
                throw new RuntimeException("Failed to serialize tags", e);
            }
        }
        
        Page<Modification> page;
        if (tagsJson != null || firearmId != null) {
            page = modificationRepository.pageQueryByFirearmAndTags(firearmId, tagsJson, pageable);
        } else {
            page = modificationRepository.findAllBy(pageable);
        }

        return PageResponse.from(page.map(ModificationResponse::from));
    }

    @Transactional(readOnly = true)
    public ModificationResponse queryById(Long id) {
        return modificationRepository.findById(id)
                .map(ModificationResponse::from)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Modification not found: " + id));
    }

    @Transactional(readOnly = true)
    public List<String> findAllTags(Long firearmId) {
        return modificationRepository.findAllTags(firearmId);
    }
}
