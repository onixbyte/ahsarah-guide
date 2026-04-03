package com.onixbyte.deltaforceguide.controller;

import com.onixbyte.deltaforceguide.domain.dto.FirearmResponse;
import com.onixbyte.deltaforceguide.domain.dto.PageResponse;
import com.onixbyte.deltaforceguide.service.FirearmService;
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
@RequestMapping("/firearms")
public class FirearmController {

    private final FirearmService firearmService;

    public FirearmController(FirearmService firearmService) {
        this.firearmService = firearmService;
    }

    @GetMapping
    public PageResponse<FirearmResponse> pageQuery(
            @RequestParam(defaultValue = "0") @Min(0) int page,
            @RequestParam(defaultValue = "20") @Min(1) @Max(100) int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "DESC") Sort.Direction direction
    ) {
        return firearmService.pageQuery(PageRequest.of(page, size, Sort.by(direction, sortBy)));
    }

    @GetMapping("/{id}")
    public FirearmResponse queryById(@PathVariable Long id) {
        return firearmService.queryById(id);
    }
}

