package com.gdg.planpal.domain.schedule.dto.response;

import com.gdg.planpal.domain.map.dto.response.MapPinResponse;

import java.time.LocalDateTime;

public record ScheduleResponse(Long id, LocalDateTime startTime, LocalDateTime endTime, MapPinResponse mapPin) {}

