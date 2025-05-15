package com.gdg.planpal.domain.map.dao;

import com.gdg.planpal.domain.map.domain.pin.MapPin;
import com.gdg.planpal.domain.map.domain.pin.StarMapPin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MapPinRepository extends JpaRepository<MapPin, Long> {

    @Query("""
        SELECT DISTINCT p
        FROM StarMapPin p
        LEFT JOIN FETCH p.schedules
        WHERE p.mapBoard.id = :mapBoardId
    """)
    List<StarMapPin> findAllWithSchedulesByMapBoardId(@Param("mapBoardId") Long mapBoardId);


    List<MapPin> findByMapBoardId(Long mapBoardId);
}