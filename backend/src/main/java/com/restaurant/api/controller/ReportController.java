package com.restaurant.api.controller;

import com.restaurant.api.dto.dashboard.DashboardSummaryResponse;
import com.restaurant.api.dto.dashboard.DailyRevenueResponse;
import com.restaurant.api.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * API Dashboard (Module 09).
 */
@RestController
@RequestMapping("/api/report")
@RequiredArgsConstructor
public class ReportController {

    private final ReportService reportService;

    /**
     * API tổng quan Dashboard
     * GET /api/report/summary
     */
    @GetMapping("/summary")
    public DashboardSummaryResponse getSummary() {
        return reportService.getSummary();
    }

    /**
     * API doanh thu theo ngày
     * GET /api/report/daily?days=7
     */
    @GetMapping("/daily")
    public List<DailyRevenueResponse> getDaily(
            @RequestParam(defaultValue = "7") int days
    ) {
        return reportService.getDaily(days);
    }
}
