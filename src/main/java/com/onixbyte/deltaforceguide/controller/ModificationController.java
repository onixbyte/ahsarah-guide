package com.onixbyte.deltaforceguide.controller;

import com.onixbyte.deltaforceguide.domain.dto.ModificationResponse;
import com.onixbyte.deltaforceguide.domain.dto.PageResponse;
import com.onixbyte.deltaforceguide.service.ModificationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Positive;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "改装管理", description = "对枪械改装的管理")
@RestController
@RequestMapping("/modifications")
public class ModificationController {

    private final ModificationService modificationService;

    public ModificationController(ModificationService modificationService) {
        this.modificationService = modificationService;
    }

    @Operation(description = "分页查询改装信息")
    @Validated
    @GetMapping
    public PageResponse<ModificationResponse> pageQuery(
            @RequestParam(defaultValue = "0") @Min(0) int page,
            @RequestParam(defaultValue = "20") @Min(1) @Max(100) int size,
            @RequestParam(required = false) @Positive Long firearmId,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "DESC") Sort.Direction direction,
            @RequestParam(required = false) List<String> tags
    ) {
        return modificationService.pageQuery(firearmId, tags, PageRequest.of(page, size, Sort.by(direction, sortBy)));
    }

    @Operation(description = "查询指定改装的信息")
    @GetMapping("/{id}")
    public ModificationResponse queryById(@PathVariable Long id) {
        return modificationService.queryById(id);
    }
}
