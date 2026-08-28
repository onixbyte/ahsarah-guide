package com.onixbyte.ahsarahguide.service;

import com.onixbyte.ahsarahguide.domain.dto.DailyPasswordResponse;
import com.onixbyte.ahsarahguide.manager.DailyPasswordManager;
import org.springframework.stereotype.Service;

/**
 * Service for generating and caching daily rotation passwords.
 *
 * @author zihluwang
 */
@Service
public class DailyPasswordService {

    private final DailyPasswordManager dailyPasswordManager;

    public DailyPasswordService(DailyPasswordManager dailyPasswordManager) {
        this.dailyPasswordManager = dailyPasswordManager;
    }

    /**
     * Retrieves the daily password for the current day.
     * @return the daily password response
     */
    public DailyPasswordResponse getDailyPassword() {
        return dailyPasswordManager.getDailyPassword();
    }
}
