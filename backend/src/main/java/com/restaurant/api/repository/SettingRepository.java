package com.restaurant.api.repository;

import com.restaurant.api.entity.Setting;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository Setting
 */
public interface SettingRepository extends JpaRepository<Setting, Long> {
}
