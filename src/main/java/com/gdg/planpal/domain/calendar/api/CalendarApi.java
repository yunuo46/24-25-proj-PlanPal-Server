package com.gdg.planpal.domain.calendar.api;

import com.gdg.planpal.domain.calendar.application.CalendarService;
import com.gdg.planpal.domain.map.domain.pin.StarMapPinSchedule;
import com.gdg.planpal.domain.map.dto.request.ScheduleRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/calendar")
@RequiredArgsConstructor
public class CalendarApi {

    private final CalendarService calendarService;

    @GetMapping("/map/{mapId}/schedules")
    public List<StarMapPinSchedule> getSchedules(@PathVariable Long mapId) {
        return calendarService.getSchedulesByMapId(mapId);
    }

    @PostMapping("/pin/{pinId}/schedules")
    public void addSchedule(@PathVariable Long pinId, @RequestBody ScheduleRequest request){
        calendarService.addSchedule(pinId, request);
    }

    @PutMapping("/schedules/{scheduleId}")
    public void updateSchedule(@PathVariable Long scheduleId, @RequestBody ScheduleRequest request){
        calendarService.updateSchedule(scheduleId, request);
    }

    @DeleteMapping("/schedules/{scheduleId}")
    public void deleteSchedule(@PathVariable Long scheduleId) {
        calendarService.deleteSchedule(scheduleId);
    }
}
