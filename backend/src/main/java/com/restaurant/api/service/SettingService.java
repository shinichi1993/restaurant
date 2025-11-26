package com.restaurant.api.service;

import com.restaurant.api.dto.SettingRequest;
import com.restaurant.api.dto.SettingResponse;
import com.restaurant.api.entity.Setting;
import com.restaurant.api.repository.SettingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * Service xử lý logic lấy & cập nhật cấu hình hệ thống
 */
@Service
@RequiredArgsConstructor
public class SettingService {

    private final SettingRepository settingRepository;

    // Lấy bản ghi setting duy nhất (id = 1)
    public SettingResponse getSetting() {
        Setting setting = settingRepository.findById(1L)
                .orElseGet(() -> settingRepository.save(
                        Setting.builder()
                                .restaurantName("Quán ăn")
                                .updatedAt(LocalDateTime.now())
                                .build()
                ));

        return toResponse(setting);
    }

    // Cập nhật cấu hình
    public SettingResponse updateSetting(SettingRequest req) {
        Setting setting = settingRepository.findById(1L)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy cấu hình!"));

        setting.setRestaurantName(req.getRestaurantName());
        setting.setRestaurantAddress(req.getRestaurantAddress());
        setting.setRestaurantPhone(req.getRestaurantPhone());
        setting.setRestaurantEmail(req.getRestaurantEmail());
        setting.setLogoUrl(req.getLogoUrl());

        setting.setVatPercent(req.getVatPercent());
        setting.setServicePercent(req.getServicePercent());
        setting.setOpeningTime(req.getOpeningTime());
        setting.setClosingTime(req.getClosingTime());
        setting.setTableCount(req.getTableCount());

        setting.setInvoiceHeader(req.getInvoiceHeader());
        setting.setInvoiceFooter(req.getInvoiceFooter());
        setting.setUpdatedAt(LocalDateTime.now());

        settingRepository.save(setting);
        return toResponse(setting);
    }

    // Convert Entity → Response
    private SettingResponse toResponse(Setting s) {
        return SettingResponse.builder()
                .id(s.getId())
                .restaurantName(s.getRestaurantName())
                .restaurantAddress(s.getRestaurantAddress())
                .restaurantPhone(s.getRestaurantPhone())
                .restaurantEmail(s.getRestaurantEmail())
                .logoUrl(s.getLogoUrl())
                .vatPercent(s.getVatPercent())
                .servicePercent(s.getServicePercent())
                .openingTime(s.getOpeningTime())
                .closingTime(s.getClosingTime())
                .tableCount(s.getTableCount())
                .invoiceHeader(s.getInvoiceHeader())
                .invoiceFooter(s.getInvoiceFooter())
                .build();
    }
}
