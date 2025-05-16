package com.gdg.planpal.domain.schedule.application;

import com.gdg.planpal.domain.map.dto.response.MapPinResponse;
import com.gdg.planpal.domain.schedule.dao.StarMapPinScheduleRepository;
import com.gdg.planpal.domain.map.application.factory.MapPinFactoryRouter;
import com.gdg.planpal.domain.map.dao.MapPinRepository;
import com.gdg.planpal.domain.map.domain.pin.*;
import com.gdg.planpal.domain.map.dto.request.ScheduleRequest;
import com.gdg.planpal.domain.schedule.domain.StarMapPinSchedule;
import com.gdg.planpal.domain.schedule.dto.response.ScheduleResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ScheduleService {

    private final StarMapPinScheduleRepository scheduleRepository;
    private final MapPinRepository mapPinRepository;
    private final MapPinFactoryRouter mapPinFactoryRouter;

    @Transactional(readOnly = true)
    public List<ScheduleResponse> getSchedulesByMapId(Long mapId) {
        return scheduleRepository.findAllByMapId(mapId).stream()
                .map(schedule -> new ScheduleResponse(
                        schedule.getId(),
                        schedule.getStartTime(),
                        schedule.getEndTime(),
                        MapPinResponse.from(schedule.getMapPin())
                ))
                .toList();
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
                    .user(pin.getUser())
                    .placeId(pin.getPlaceId())
                    .title(pin.getTitle())
                    .address(pin.getAddress())
                    .content(pin.getContent())
                    .type(pin.getType())
                    .rating(pin.getRating())
                    .build();
            mapPinRepository.delete(pin);
            mapPinRepository.save(newHeartPin);
        }
    }
}

