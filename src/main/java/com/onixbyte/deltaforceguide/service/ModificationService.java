package com.onixbyte.deltaforceguide.service;

import com.onixbyte.deltaforceguide.domain.dto.AccessoryRequest;
import com.onixbyte.deltaforceguide.domain.dto.ModificationRequest;
import com.onixbyte.deltaforceguide.domain.dto.ModificationResponse;
import com.onixbyte.deltaforceguide.domain.dto.PageResponse;
import com.onixbyte.deltaforceguide.domain.dto.TuningRequest;
import com.onixbyte.deltaforceguide.domain.entity.Accessory;
import com.onixbyte.deltaforceguide.domain.entity.Firearm;
import com.onixbyte.deltaforceguide.domain.entity.Modification;
import com.onixbyte.deltaforceguide.domain.entity.User;
import com.onixbyte.deltaforceguide.domain.entity.Tuning;
import com.onixbyte.deltaforceguide.manager.ModificationManager;
import com.onixbyte.deltaforceguide.repository.FirearmRepository;
import com.onixbyte.deltaforceguide.repository.ModificationRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * Service handling modification business logic including CRUD, batch operations, and tag filtering.
 *
 * @author zihluwang
 */
@Service
public class ModificationService {

    private final ModificationRepository modificationRepository;
    private final FirearmRepository firearmRepository;
    private final ModificationManager modificationManager;
    private final ObjectMapper objectMapper;

    public ModificationService(
            ModificationRepository modificationRepository,
            FirearmRepository firearmRepository,
            ModificationManager modificationManager,
            ObjectMapper objectMapper
    ) {
        this.modificationRepository = modificationRepository;
        this.firearmRepository = firearmRepository;
        this.modificationManager = modificationManager;
        this.objectMapper = objectMapper;
    }

    /**
     * Queries modifications with optional firearm and tag filters.
     *
     * @param firearmId optional firearm ID filter
     * @param tags optional tag list filter
     * @param pageable pagination parameters
     * @return a paginated response of modification records
     */
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

    /**
     * Finds a modification by its ID.
     *
     * @param id the modification ID
     * @return the modification response
     */
    public ModificationResponse queryById(Long id) {
        return modificationRepository.findById(id)
                .map(ModificationResponse::from)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Modification not found: " + id));
    }

    /**
     * Finds all unique tags across modifications, optionally scoped to a firearm.
     *
     * @param firearmId optional firearm ID to scope the tag search
     * @return list of unique tag strings
     */
    public List<String> findAllTags(Long firearmId) {
        return modificationRepository.findAllTags(firearmId);
    }

    /**
     * Creates a new modification for a given firearm.
     *
     * @param request the modification creation request
     * @param user    the authenticated user creating the modification
     * @return the created modification response
     */
    public ModificationResponse create(ModificationRequest request, User user) {
        return modificationManager.create(request, user);
    }

    /**
     * Creates multiple modifications in a single batch operation.
     *
     * @param requests list of modification creation requests
     * @param user     the authenticated user creating the modifications
     * @return list of created modification responses
     */
    public List<ModificationResponse> batchCreate(List<ModificationRequest> requests, User user) {
        return modificationManager.batchCreate(requests, user);
    }

    /**
     * Updates an existing modification identified by ID.
     *
     * @param id the modification ID
     * @param request the updated modification data
     * @return the updated modification response
     */
    public ModificationResponse update(Long id, ModificationRequest request) {
        Modification modification = modificationRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Modification not found: " + id));
        Firearm firearm = firearmRepository.findById(request.firearmId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Firearm not found: " + request.firearmId()));

        modification.setFirearm(firearm);
        modification.setName(request.name());
        modification.setCode(request.code());
        modification.setTags(safeTags(request.tags()));
        modification.setAccessories(toAccessories(request.accessories()));
        modification.setNote(request.note());
        modification.setAuthor(request.author());
        modification.setVideoUrl(request.videoUrl());

        return ModificationResponse.from(modificationRepository.save(modification));
    }

    /**
     * Deletes a modification by its ID.
     *
     * @param id the modification ID to delete
     */
    public void delete(Long id) {
        Modification modification = modificationRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Modification not found: " + id));
        modificationRepository.delete(modification);
    }

    /**
     * Deletes multiple modifications in a single batch operation.
     *
     * @param ids list of modification IDs to delete
     */
    public void batchDelete(List<Long> ids) {
        Set<Long> uniqueIds = new LinkedHashSet<>(ids);
        List<Modification> modifications = modificationRepository.findAllById(uniqueIds);

        if (modifications.size() != uniqueIds.size()) {
            Set<Long> foundIds = modifications.stream()
                    .map(Modification::getId)
                    .collect(java.util.stream.Collectors.toSet());
            List<Long> missingIds = uniqueIds.stream()
                    .filter(id -> !foundIds.contains(id))
                    .toList();
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Modification not found: " + missingIds);
        }

        modificationRepository.deleteAllInBatch(modifications);
    }

    private List<String> safeTags(List<String> tags) {
        return tags == null ? new ArrayList<>() : tags;
    }

    private List<Accessory> toAccessories(List<AccessoryRequest> accessoryRequests) {
        if (accessoryRequests == null) {
            return new ArrayList<>();
        }

        return accessoryRequests.stream()
                .map(this::toAccessory)
                .toList();
    }

    private Accessory toAccessory(AccessoryRequest request) {
        Accessory accessory = new Accessory();
        accessory.setSlotName(request.slotName());
        accessory.setAccessoryName(request.accessoryName());
        accessory.setTunings(toTunings(request.tunings()));
        return accessory;
    }

    private List<Tuning> toTunings(List<TuningRequest> tuningRequests) {
        if (tuningRequests == null) {
            return new ArrayList<>();
        }

        return tuningRequests.stream()
                .map(this::toTuning)
                .toList();
    }

    private Tuning toTuning(TuningRequest request) {
        Tuning tuning = new Tuning();
        tuning.setTuningName(request.tuningName());
        tuning.setTuningValue(request.tuningValue());
        return tuning;
    }
}
