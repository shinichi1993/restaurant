package com.restaurant.api.service;

import com.restaurant.api.dto.dashboard.DashboardSummaryResponse;
import com.restaurant.api.dto.dashboard.DailyRevenueResponse;
import com.restaurant.api.repository.InvoiceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.sql.Date;
import java.time.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Service xử lý dữ liệu Dashboard.
 */
@Service
@RequiredArgsConstructor
public class ReportService {

    private final InvoiceRepository invoiceRepository;

    /**
     * Lấy dữ liệu tổng quan Dashboard.
     */
    public DashboardSummaryResponse getSummary() {

        long totalOrders = invoiceRepository.getTotalCount();
        BigDecimal totalRevenue = invoiceRepository.getTotalRevenue();

        long paidOrders = invoiceRepository.countByStatus("PAID");
        long pendingOrders = invoiceRepository.countByStatus("UNPAID");

        LocalDate today = LocalDate.now();
        LocalDateTime from = today.atStartOfDay();
        LocalDateTime to = today.atTime(LocalTime.MAX);

        BigDecimal todayRevenue = invoiceRepository.sumPaidBetween(from, to);
        long todayOrders = invoiceRepository.countPaidBetween(from, to);

        return DashboardSummaryResponse.builder()
                .totalOrders(totalOrders)
                .totalRevenue(totalRevenue)
                .paidOrders(paidOrders)
                .pendingOrders(pendingOrders)
                .todayRevenue(todayRevenue)
                .todayOrders(todayOrders)
                .build();
    }

    /**
     * Lấy doanh thu theo từng ngày trong N ngày gần nhất.
     */
    public List<DailyRevenueResponse> getDaily(int days) {

        if (days <= 0) days = 7;

        LocalDate toDate = LocalDate.now();
        LocalDate fromDate = toDate.minusDays(days - 1L);

        LocalDateTime from = fromDate.atStartOfDay();
        LocalDateTime to = toDate.atTime(LocalTime.MAX);

        List<Object[]> raw = invoiceRepository.getDailyRevenue(from, to);

        List<DailyRevenueResponse> result = new ArrayList<>();

        for (Object[] row : raw) {

            LocalDate date;
            if (row[0] instanceof LocalDate d) {
                date = d;
            } else {
                date = ((Date) row[0]).toLocalDate();
            }

            Object revenueObj = row[1];
            BigDecimal revenue;
            // Nếu DB trả về BigDecimal
            if (revenueObj instanceof BigDecimal bd) {
                revenue = bd;

            // Nếu DB trả về Double
            } else if (revenueObj instanceof Double d) {
                revenue = BigDecimal.valueOf(d);

            // Nếu DB trả về Integer / Long / Number bất kỳ
            } else if (revenueObj instanceof Number n) {
                revenue = BigDecimal.valueOf(n.doubleValue());

            // Fallback (không nên xảy ra)
            } else {
                revenue = BigDecimal.ZERO;
            }

            long count = ((Number) row[2]).longValue();

            result.add(DailyRevenueResponse.builder()
                    .date(date)
                    .revenue(revenue)
                    .orderCount(count)
                    .build());
        }

        return result;
    }
}
