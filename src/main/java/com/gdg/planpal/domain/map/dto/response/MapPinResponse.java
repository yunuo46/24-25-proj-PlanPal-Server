package com.gdg.planpal.domain.map.dto.response;

import com.gdg.planpal.domain.map.domain.pin.MapPin;
import com.gdg.planpal.domain.map.domain.IconType;

public record MapPinResponse(
        Long id,
        Double lat,
        Double lng,
        String content,
        IconType iconType
) {
    public static MapPinResponse from(MapPin pin) {
        return new MapPinResponse(
                pin.getId(),
                pin.getCoordinates().getLat(),
                pin.getCoordinates().getLng(),
                pin.getContent(),
                pin.getIconType()
        );
    }
}