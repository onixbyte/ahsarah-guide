package com.onixbyte.deltaforceguide.controller;

import com.onixbyte.deltaforceguide.domain.dto.FirearmRequest;
import com.onixbyte.deltaforceguide.domain.dto.FirearmResponse;
import com.onixbyte.deltaforceguide.domain.dto.PageResponse;
import com.onixbyte.deltaforceguide.enumeration.FirearmType;
import com.onixbyte.deltaforceguide.service.FirearmService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Tag(name = "武器管理", description = "与武器有关的操作")
@RestController
@RequestMapping("/firearms")
public class FirearmController {

    private final FirearmService firearmService;

    public FirearmController(FirearmService firearmService) {
        this.firearmService = firearmService;
    }

    @Operation(description = "获取分页武器数据")
    @Validated
    @GetMapping
    public PageResponse<FirearmResponse> pageQuery(
            @RequestParam(defaultValue = "0") @Min(0) int page,
            @RequestParam(defaultValue = "20") @Min(1) @Max(100) int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "DESC") Sort.Direction direction,
            @RequestParam(required = false) FirearmType type
    ) {
        return firearmService.pageQuery(type, PageRequest.of(page, size, Sort.by(direction, sortBy)));
    }

    @Operation(description = "获取指定武器的数据")
    @GetMapping("/{id}")
    public FirearmResponse queryById(@PathVariable Long id) {
        return firearmService.queryById(id);
    }

    @PostMapping
    public FirearmResponse addFirearm(@Validated @RequestBody FirearmRequest request) {
        return firearmService.addFirearm(request);
    }
}

