package com.onixbyte.deltaforceguide.controller;

import com.onixbyte.deltaforceguide.service.AppService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "版本信息")
@RestController
@RequestMapping("/versions")
public class VersionController {

    private final AppService appService;

    public VersionController(AppService appService) {
        this.appService = appService;
    }

    @Operation(description = "获取当前应用版本号")
    @GetMapping
    public String getVersion() {
        return appService.getVersion();
    }
}
