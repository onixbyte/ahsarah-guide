package com.onixbyte.deltaforceguide.service;

import com.onixbyte.deltaforceguide.domain.dto.AccessoryRequest;
import com.onixbyte.deltaforceguide.domain.dto.ModificationRequest;
import com.onixbyte.deltaforceguide.domain.dto.ModificationResponse;
import com.onixbyte.deltaforceguide.domain.dto.PageResponse;
import com.onixbyte.deltaforceguide.domain.dto.TuningRequest;
import com.onixbyte.deltaforceguide.domain.entity.Accessory;
import com.onixbyte.deltaforceguide.domain.entity.Firearm;
import com.onixbyte.deltaforceguide.domain.entity.Modification;
import com.onixbyte.deltaforceguide.domain.entity.Tuning;
import com.onixbyte.deltaforceguide.repository.FirearmRepository;
import com.onixbyte.deltaforceguide.repository.ModificationRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
public class ModificationService {

    private final ModificationRepository modificationRepository;
    private final FirearmRepository firearmRepository;
    private final ObjectMapper objectMapper;

    public ModificationService(
            ModificationRepository modificationRepository,
            FirearmRepository firearmRepository,
            ObjectMapper objectMapper
    ) {
        this.modificationRepository = modificationRepository;
        this.firearmRepository = firearmRepository;
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

    @Transactional
    public ModificationResponse create(ModificationRequest request) {
        Firearm firearm = firearmRepository.findById(request.firearmId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Firearm not found: " + request.firearmId()));

        Modification modification = toEntity(request, firearm);
        return ModificationResponse.from(modificationRepository.save(modification));
    }

    @Transactional
    public List<ModificationResponse> batchCreate(List<ModificationRequest> requests) {
        Set<Long> firearmIds = requests.stream()
                .map(ModificationRequest::firearmId)
                .collect(java.util.stream.Collectors.toCollection(LinkedHashSet::new));

        Map<Long, Firearm> firearmMap = new HashMap<>();
        firearmRepository.findAllById(firearmIds).forEach(firearm -> firearmMap.put(firearm.getId(), firearm));

        if (firearmMap.size() != firearmIds.size()) {
            List<Long> missingFirearmIds = firearmIds.stream()
                    .filter(id -> !firearmMap.containsKey(id))
                    .toList();
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Firearm not found: " + missingFirearmIds);
        }

        List<Modification> modifications = requests.stream()
                .map(request -> toEntity(request, firearmMap.get(request.firearmId())))
                .toList();
        return modificationRepository.saveAll(modifications)
                .stream()
                .map(ModificationResponse::from)
                .toList();
    }

    @Transactional
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

    @Transactional
    public void delete(Long id) {
        Modification modification = modificationRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Modification not found: " + id));
        modificationRepository.delete(modification);
    }

    @Transactional
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

    private Modification toEntity(ModificationRequest request, Firearm firearm) {
        return Modification.builder()
                .firearm(firearm)
                .name(request.name())
                .code(request.code())
                .tags(safeTags(request.tags()))
                .accessories(toAccessories(request.accessories()))
                .note(request.note())
                .author(request.author())
                .videoUrl(request.videoUrl())
                .build();
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
