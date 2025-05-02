package com.gdg.planpal.domain.schedule.dao;

import com.gdg.planpal.domain.schedule.domain.StarMapPinSchedule;
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
