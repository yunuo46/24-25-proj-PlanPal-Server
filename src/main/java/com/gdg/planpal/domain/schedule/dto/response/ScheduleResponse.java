package com.gdg.planpal.domain.schedule.dto.response;

import java.time.LocalDateTime;

public record ScheduleResponse(Long id, Long mapPinId, LocalDateTime startTime, LocalDateTime endTime) {}

