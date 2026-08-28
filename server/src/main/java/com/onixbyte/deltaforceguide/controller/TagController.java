package com.onixbyte.deltaforceguide.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.onixbyte.deltaforceguide.service.ModificationService;

import java.util.List;

/**
 * REST controller for retrieving available modification tags.
 *
 * @author zihluwang
 */
@Tag(name = "标签管理", description = "管理标签信息")
@RestController
@RequestMapping("/tags")
public class TagController {

    private final ModificationService modificationService;

    public TagController(ModificationService modificationService) {
        this.modificationService = modificationService;
    }

    @Operation(description = "查询指定武器或所有武器的标签")
    @GetMapping
    public List<String> getTags(@RequestParam(required = false) Long firearmId) {
        return modificationService.findAllTags(firearmId);
    }
}
