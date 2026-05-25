package com.onixbyte.deltaforceguide.service;

import com.onixbyte.deltaforceguide.domain.dto.DailyPasswordResponse;
import com.onixbyte.deltaforceguide.manager.DailyPasswordManager;
import org.springframework.stereotype.Service;

@Service
public class DailyPasswordService {

    private final DailyPasswordManager dailyPasswordManager;

    public DailyPasswordService(DailyPasswordManager dailyPasswordManager) {
        this.dailyPasswordManager = dailyPasswordManager;
    }

    public DailyPasswordResponse getDailyPassword() {
        return dailyPasswordManager.getDailyPassword();
    }
}
