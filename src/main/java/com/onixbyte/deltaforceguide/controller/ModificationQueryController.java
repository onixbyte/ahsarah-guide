package com.onixbyte.deltaforceguide.controller;

import com.onixbyte.deltaforceguide.domain.dto.ModificationResponse;
import com.onixbyte.deltaforceguide.domain.dto.PageResponse;
import com.onixbyte.deltaforceguide.service.ModificationQueryService;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RestController
@RequestMapping("/api/v1/modifications")
public class ModificationQueryController {

    private final ModificationQueryService modificationQueryService;

    public ModificationQueryController(ModificationQueryService modificationQueryService) {
        this.modificationQueryService = modificationQueryService;
    }

    @GetMapping
    public PageResponse<ModificationResponse> pageQuery(
            @RequestParam(defaultValue = "0") @Min(0) int page,
            @RequestParam(defaultValue = "20") @Min(1) @Max(100) int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "DESC") Sort.Direction direction
    ) {
        return modificationQueryService.pageQuery(PageRequest.of(page, size, Sort.by(direction, sortBy)));
    }

    @GetMapping("/{id}")
    public ModificationResponse queryById(@PathVariable Long id) {
        return modificationQueryService.queryById(id);
    }
}

