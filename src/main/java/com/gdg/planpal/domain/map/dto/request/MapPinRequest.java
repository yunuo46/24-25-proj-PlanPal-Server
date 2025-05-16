package com.gdg.planpal.domain.map.dto.request;

import com.gdg.planpal.domain.map.domain.IconType;
import jakarta.validation.constraints.NotNull;

public record MapPinRequest(
        @NotNull(message = "제목은 필수 항목입니다.") String title,
        @NotNull(message = "주소는 필수 항목입니다.") String address,
        @NotNull(message = "상세 설명은 필수 항목입니다.") String content,
        @NotNull(message = "타입은 필수 항목입니다.") String type,
        @NotNull(message = "별점은 필수 항목입니다.") double rating,
        @NotNull(message = "아이콘 타입은 필수 항목입니다.") IconType iconType,
        @NotNull(message = "place id는 필수 항목입니다.") String placeId,
        @NotNull(message = "위도는 필수 항목입니다.") Double lat,
        @NotNull(message = "경도는 필수 항목입니다.") Double lng,
        ScheduleRequest schedule
) {
}
