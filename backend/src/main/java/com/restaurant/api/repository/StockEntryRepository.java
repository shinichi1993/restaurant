package com.restaurant.api.repository;

import com.restaurant.api.entity.StockEntry;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StockEntryRepository extends JpaRepository<StockEntry, Long> {

}
