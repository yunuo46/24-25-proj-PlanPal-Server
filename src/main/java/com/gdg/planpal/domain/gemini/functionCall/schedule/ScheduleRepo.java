package com.gdg.planpal.domain.gemini.functionCall.schedule;

import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ScheduleRepo {

    public List<Schedule> getSchedule(){
        return List.of(
                new Schedule(1L,"melbourne zoo","2025-05-07","14:30","2025-05-07","17:30"),
                new Schedule(2L,"Bondi Beach","2025-05-07","11:00","2025-05-07","13:00"));
    }

}
