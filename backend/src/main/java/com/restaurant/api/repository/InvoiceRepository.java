package com.restaurant.api.repository;

import com.restaurant.api.entity.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Repository Invoice – xử lý truy vấn bảng invoice.
 * Bao gồm:
 * - Lấy danh sách
 * - Lọc theo ngày tạo
 * - Tìm kiếm theo mã hóa đơn hoặc người tạo
 */
public interface InvoiceRepository extends JpaRepository<Invoice, Long> {

    /**
     * Lọc hóa đơn theo:
     * - Khoảng thời gian (created_at)
     * - Từ khóa (mã hóa đơn hoặc created_by)
     */
    @Query("""
        SELECT i FROM Invoice i
        WHERE i.createdAt >= :fromDate
          AND i.createdAt <= :toDate
          AND (
                :keyword = '' 
                OR LOWER(i.code) LIKE LOWER(CONCAT('%', :keyword, '%'))
                OR LOWER(i.createdBy) LIKE LOWER(CONCAT('%', :keyword, '%'))
          )
        ORDER BY i.createdAt DESC
    """)
    List<Invoice> filterInvoices(
            @Param("fromDate") LocalDateTime fromDate,
            @Param("toDate") LocalDateTime toDate,
            @Param("keyword") String keyword
    );

    /**
     * Tổng doanh thu từ toàn bộ hóa đơn.
     */
    @Query("SELECT COALESCE(SUM(i.totalAmount), 0) FROM Invoice i")
    BigDecimal getTotalRevenue();

    /**
     * Tổng số hóa đơn.
     */
    @Query("SELECT COUNT(i) FROM Invoice i")
    long getTotalCount();

    /**
     * Đếm số hóa đơn theo trạng thái.
     */
    @Query("SELECT COUNT(i) FROM Invoice i WHERE i.status = :status")
    long countByStatus(@Param("status") String status);

    /**
     * Tổng doanh thu của các hóa đơn đã thanh toán trong khoảng thời gian.
     */
    @Query("""
        SELECT COALESCE(SUM(i.totalAmount), 0)
        FROM Invoice i
        WHERE i.status = 'PAID'
          AND i.paidAt BETWEEN :from AND :to
    """)
    BigDecimal sumPaidBetween(
            @Param("from") LocalDateTime from,
            @Param("to") LocalDateTime to
    );

    /**
     * Đếm số hóa đơn đã thanh toán trong khoảng thời gian.
     */
    @Query("""
        SELECT COUNT(i)
        FROM Invoice i
        WHERE i.status = 'PAID'
          AND i.paidAt BETWEEN :from AND :to
    """)
    long countPaidBetween(
            @Param("from") LocalDateTime from,
            @Param("to") LocalDateTime to
    );

    /**
     * Lấy doanh thu theo ngày (group theo DATE(paidAt)).
     */
    @Query("""
        SELECT FUNCTION('DATE', i.paidAt) AS day,
               COALESCE(SUM(i.totalAmount), 0),
               COUNT(i)
        FROM Invoice i
        WHERE i.status = 'PAID'
          AND i.paidAt BETWEEN :from AND :to
        GROUP BY FUNCTION('DATE', i.paidAt)
        ORDER BY FUNCTION('DATE', i.paidAt)
    """)
    List<Object[]> getDailyRevenue(
            @Param("from") LocalDateTime from,
            @Param("to") LocalDateTime to
    );

}

