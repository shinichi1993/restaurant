package com.restaurant.api.repository;

import com.restaurant.api.entity.InvoiceItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Repository InvoiceItem – dùng để lấy chi tiết món ăn theo invoice.
 * Không cần custom query phức tạp.
 */
public interface InvoiceItemRepository extends JpaRepository<InvoiceItem, Long> {

    /**
     * Lấy toàn bộ danh sách món ăn theo invoice_id
     */
    List<InvoiceItem> findByInvoiceId(Long invoiceId);
}
