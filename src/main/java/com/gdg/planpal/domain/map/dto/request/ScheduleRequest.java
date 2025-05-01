package com.gdg.planpal.domain.map.dto.request;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record ScheduleRequest(
        @NotNull(message = "pin id는 필수입니다.") Long pinId,
        @NotNull(message = "시작 시간은 필수입니다.") LocalDateTime startTime,
        @NotNull(message = "종료 시간은 필수입니다.") LocalDateTime endTime
) {
}
