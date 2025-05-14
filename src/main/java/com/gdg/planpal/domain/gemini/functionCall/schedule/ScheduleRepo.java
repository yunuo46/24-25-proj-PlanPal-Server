package com.gdg.planpal.domain.gemini.functionCall.schedule;

import com.gdg.planpal.domain.schedule.dao.StarMapPinScheduleRepository;
import com.gdg.planpal.domain.schedule.domain.StarMapPinSchedule;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class ScheduleRepo {
    private final StarMapPinScheduleRepository scheduleRepository;

    public List<StarMapPinSchedule> getSchedule(Long mapId){



        return scheduleRepository.findAllByMapId(mapId);
    }

}
