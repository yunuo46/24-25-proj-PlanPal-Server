package com.gdg.planpal.domain.map.dto.request;

import com.gdg.planpal.domain.map.domain.pin.StarMapPin;
import com.gdg.planpal.domain.schedule.domain.StarMapPinSchedule;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record ScheduleRequest(
        @NotNull(message = "시작 시간은 필수입니다.") LocalDateTime startTime,
        @NotNull(message = "종료 시간은 필수입니다.") LocalDateTime endTime
) {
    public StarMapPinSchedule from(StarMapPin pin) {
        return StarMapPinSchedule.builder()
                .mapPin(pin)
                .startTime(startTime)
                .endTime(endTime)
                .build();
    }
}
