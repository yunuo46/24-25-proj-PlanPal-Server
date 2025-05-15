package com.gdg.planpal.domain.map.dto.response;

import com.gdg.planpal.domain.map.domain.pin.MapPin;
import com.gdg.planpal.domain.map.domain.IconType;

public record MapPinResponse(
        Long id,
        Long userId,
        Double lat,
        Double lng,
        String title,
        String address,
        String content,
        String type,
        double rating,
        IconType iconType
) {
    public static MapPinResponse from(MapPin pin) {
        return new MapPinResponse(
                pin.getId(),
                pin.getUser().getId(),
                pin.getCoordinates().getLat(),
                pin.getCoordinates().getLng(),
                pin.getTitle(),
                pin.getAddress(),
                pin.getContent(),
                pin.getType(),
                pin.getRating(),
                pin.getIconType()
        );
    }
}