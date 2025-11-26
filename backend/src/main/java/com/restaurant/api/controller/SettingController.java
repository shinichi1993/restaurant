package com.restaurant.api.controller;

import com.restaurant.api.dto.SettingRequest;
import com.restaurant.api.dto.SettingResponse;
import com.restaurant.api.service.SettingService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * API Setting – Lấy và cập nhật cấu hình hệ thống
 */
@RestController
@RequestMapping("/api/settings")
@RequiredArgsConstructor
public class SettingController {

    private final SettingService settingService;

    // Lấy cấu hình hệ thống
    @GetMapping
    public SettingResponse getSetting() {
        return settingService.getSetting();
    }

    // Cập nhật cấu hình
    @PutMapping
    public SettingResponse updateSetting(@RequestBody SettingRequest req) {
        return settingService.updateSetting(req);
    }
}
