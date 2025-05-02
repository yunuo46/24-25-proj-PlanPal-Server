package com.gdg.planpal.domain.calendar.dao;

import com.gdg.planpal.domain.map.domain.pin.StarMapPinSchedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

public interface StarMapPinScheduleRepository extends JpaRepository<StarMapPinSchedule, Long> {

    @Query("""
        SELECT s FROM StarMapPinSchedule s
        JOIN FETCH s.mapPin p
        WHERE p.mapBoard.id = :mapId
    """)
    List<StarMapPinSchedule> findAllByMapId(Long mapId);
}
