package com.gdg.planpal.domain.map.dao;

import com.gdg.planpal.domain.map.domain.pin.MapPin;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MapPinRepository extends JpaRepository<MapPin, Long> {
}
