package com.gdg.planpal.domain.calendar.application;

import com.gdg.planpal.domain.calendar.dao.StarMapPinScheduleRepository;
import com.gdg.planpal.domain.map.application.factory.MapPinFactoryRouter;
import com.gdg.planpal.domain.map.dao.MapPinRepository;
import com.gdg.planpal.domain.map.domain.pin.*;
import com.gdg.planpal.domain.map.dto.request.ScheduleRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CalendarService {

    private final StarMapPinScheduleRepository scheduleRepository;
    private final MapPinRepository mapPinRepository;
    private final MapPinFactoryRouter mapPinFactoryRouter;

    @Transactional(readOnly = true)
    public List<StarMapPinSchedule> getSchedulesByMapId(Long mapId) {
        return scheduleRepository.findAllByMapId(mapId);
    }

    @Transactional
    public void addSchedule(Long pinId, ScheduleRequest request) {
        MapPin pin = mapPinRepository.findById(pinId)
                .orElseThrow(() -> new IllegalArgumentException("MapPin not found"));

        MapPin result = mapPinFactoryRouter.addSchedule(pin, request);

        if (!result.getId().equals(pin.getId())) {
            mapPinRepository.delete(pin);
        }
        mapPinRepository.save(result);
    }

    @Transactional
    public void updateSchedule(Long scheduleId, ScheduleRequest request) {
        StarMapPinSchedule schedule = scheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new IllegalArgumentException("Schedule not found"));
        schedule.update(request);
    }

    @Transactional
    public void deleteSchedule(Long scheduleId) {
        StarMapPinSchedule schedule = scheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new IllegalArgumentException("Schedule not found"));
        scheduleRepository.delete(schedule);

        StarMapPin pin = schedule.getMapPin();
        pin.getSchedules().remove(schedule);

        if (pin.getSchedules().isEmpty()) {
            HeartMapPin newHeartPin = HeartMapPin.builder()
                    .mapBoard(pin.getMapBoard())
                    .coordinates(pin.getCoordinates())
                    .placeId(pin.getPlaceId())
                    .content(pin.getContent())
                    .build();
            mapPinRepository.delete(pin);
            mapPinRepository.save(newHeartPin);
        }
    }
}

