package com.onixbyte.deltaforceguide.manager;

import com.onixbyte.deltaforceguide.domain.entity.Accessory;
import com.onixbyte.deltaforceguide.domain.entity.Firearm;
import com.onixbyte.deltaforceguide.domain.entity.Modification;
import com.onixbyte.deltaforceguide.domain.entity.Tuning;
import com.onixbyte.deltaforceguide.domain.entity.User;
import com.onixbyte.deltaforceguide.domain.dto.AccessoryRequest;
import com.onixbyte.deltaforceguide.domain.dto.ModificationRequest;
import com.onixbyte.deltaforceguide.domain.dto.ModificationResponse;
import com.onixbyte.deltaforceguide.domain.dto.TuningRequest;
import com.onixbyte.deltaforceguide.repository.FirearmRepository;
import com.onixbyte.deltaforceguide.repository.ModificationRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;

@Component
public class ModificationManager {

    private final ModificationRepository modificationRepository;
    private final FirearmRepository firearmRepository;

    public ModificationManager(
            ModificationRepository modificationRepository,
            FirearmRepository firearmRepository
    ) {
        this.modificationRepository = modificationRepository;
        this.firearmRepository = firearmRepository;
    }

    @Transactional
    public ModificationResponse create(ModificationRequest request, User user) {
        var firearm = firearmRepository.findById(request.firearmId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Firearm not found: " + request.firearmId()));
        var modification = toEntity(request, firearm, user);
        return ModificationResponse.from(modificationRepository.save(modification));
    }

    @Transactional
    public List<ModificationResponse> batchCreate(List<ModificationRequest> requests, User user) {
        var firearmIds = requests.stream()
                .map(ModificationRequest::firearmId)
                .collect(java.util.stream.Collectors.toCollection(LinkedHashSet::new));

        Map<Long, Firearm> firearmMap = new HashMap<>();
        firearmRepository.findAllById(firearmIds)
                .forEach(firearm -> firearmMap.put(firearm.getId(), firearm));

        if (firearmMap.size() != firearmIds.size()) {
            var missing = firearmIds.stream()
                    .filter((id) -> !firearmMap.containsKey(id))
                    .toList();
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "Firearm not found: " + missing);
        }

        var modifications = requests.stream()
                .map(req -> toEntity(req, firearmMap.get(req.firearmId()), user))
                .toList();
        return modificationRepository.saveAll(modifications)
                .stream()
                .map(ModificationResponse::from)
                .toList();
    }

    public Long resolveFirearmId(Long firearmId, String firearmName) {
        if (firearmId != null) {
            return firearmId;
        }
        if (firearmName == null || firearmName.isBlank()) {
            return null;
        }
        var matches = firearmRepository.findByName(firearmName);
        if (matches.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "Firearm not found by name: " + firearmName);
        }
        return matches.getFirst().getId();
    }

    private Modification toEntity(ModificationRequest request, Firearm firearm, User user) {
        return Modification.builder()
                .firearm(firearm)
                .name(request.name())
                .code(request.code())
                .tags(request.tags())
                .accessories(toAccessories(request.accessories()))
                .note(request.note())
                .author(request.author())
                .videoUrl(request.videoUrl())
                .createBy(user == null ? null : user.getId())
                .build();
    }

    private List<Accessory> toAccessories(List<AccessoryRequest> requests) {
        if (requests == null) {
            return new ArrayList<>();
        }
        return requests.stream().map(this::toAccessory).toList();
    }

    private Accessory toAccessory(AccessoryRequest request) {
        var accessory = new Accessory();
        accessory.setSlotName(request.slotName());
        accessory.setAccessoryName(request.accessoryName());
        accessory.setTunings(toTunings(request.tunings()));
        return accessory;
    }

    private List<Tuning> toTunings(List<TuningRequest> requests) {
        if (requests == null) {
            return new ArrayList<>();
        }
        return requests.stream().map(this::toTuning).toList();
    }

    private Tuning toTuning(TuningRequest request) {
        var tuning = new Tuning();
        tuning.setTuningName(request.tuningName());
        tuning.setTuningValue(request.tuningValue());
        return tuning;
    }

    @Transactional(readOnly = true)
    public Page<Modification> findBySpec(Specification<Modification> spec, Pageable pageable) {
        return modificationRepository.findAll(spec, pageable);
    }
}
