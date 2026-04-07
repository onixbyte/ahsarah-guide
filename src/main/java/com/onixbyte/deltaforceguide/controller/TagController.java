package com.onixbyte.deltaforceguide.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.onixbyte.deltaforceguide.service.ModificationService;

import java.util.List;

@RestController
@RequestMapping("/tags")
public class TagController {

    private final ModificationService modificationService;

    public TagController(ModificationService modificationService) {
        this.modificationService = modificationService;
    }

    @GetMapping
    public List<String> getTags(@RequestParam(required = false) Long firearmId) {
        return modificationService.findAllTags(firearmId);
    }
}
