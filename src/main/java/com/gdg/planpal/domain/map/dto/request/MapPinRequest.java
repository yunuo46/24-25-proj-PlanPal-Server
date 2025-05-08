package com.gdg.planpal.domain.map.dto.request;


import com.gdg.planpal.domain.map.domain.IconType;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record MapPinRequest(
        @NotNull(message = "위도는 필수 항목입니다.") Double lat,
        @NotNull(message = "경도는 필수 항목입니다.") Double lng,
        String content,
        @NotNull(message = "아이콘 타입은 필수 항목입니다.") IconType iconType,
        @NotNull(message = "place id는 필수 항목입니다.") String placeId,
        List<ScheduleRequest> schedules
) {
}
