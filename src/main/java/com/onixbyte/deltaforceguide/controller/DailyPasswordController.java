package com.onixbyte.deltaforceguide.controller;

import com.onixbyte.deltaforceguide.domain.dto.DailyPasswordResponse;
import com.onixbyte.deltaforceguide.service.DailyPasswordService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller for retrieving daily-generated passwords.
 *
 * @author zihluwang
 */
@Tag(name = "每日密码", description = "获取每日密码信息")
@RestController
@RequestMapping("/daily-passwords")
public class DailyPasswordController {

    private final DailyPasswordService dailyPasswordService;

    public DailyPasswordController(DailyPasswordService dailyPasswordService) {
        this.dailyPasswordService = dailyPasswordService;
    }

    @Operation(description = "获取当日的每日密码数据，该数据将被缓存一天")
    @GetMapping
    public DailyPasswordResponse getDailyPassword() {
        return dailyPasswordService.getDailyPassword();
    }
}
