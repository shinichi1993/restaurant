package com.restaurant.api.repository;

import com.restaurant.api.entity.Payment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 * Repository thao tác với bảng payment.
 */
public interface PaymentRepository extends JpaRepository<Payment, Long> {

    /**
     * Tìm payment theo invoiceId.
     * Dùng cho trường hợp cần kiểm tra 1 invoice đã có payment hay chưa.
     */
    Optional<Payment> findByInvoice_Id(Long invoiceId);

    /**
     * Lấy danh sách payment theo filter:
     * - Khoảng thời gian (created_at) từ fromDate đến toDate
     * - method (nếu có)
     * - status (nếu có)
     *
     * Rule 20: fromDate/toDate truyền luôn giá trị min/max, KHÔNG truyền null.
     */
    @Query("""
        SELECT p
        FROM Payment p
        WHERE p.createdAt >= :fromDate
          AND p.createdAt <= :toDate
          AND (:method IS NULL OR p.method = :method)
          AND (:status IS NULL OR p.status = :status)
        ORDER BY p.createdAt DESC
        """)
    Page<Payment> searchPayments(
            @Param("fromDate") LocalDateTime fromDate,
            @Param("toDate") LocalDateTime toDate,
            @Param("method") String method,
            @Param("status") String status,
            Pageable pageable
    );
}
