package com.gdg.planpal.domain.map.dto.response;

import com.gdg.planpal.domain.map.domain.Coordinates;
import com.gdg.planpal.domain.map.domain.pin.MapPin;
import com.gdg.planpal.domain.map.domain.IconType;

public record MapPinResponse(
        Long id,
        String placeId,
        Double lat,
        Double lng,
        String userName,
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
                pin.getPlaceId(),
                pin.getCoordinates().getLat(),
                pin.getCoordinates().getLng(),
                pin.getUser().getName(),
                pin.getTitle(),
                pin.getAddress(),
                pin.getContent(),
                pin.getType(),
                pin.getRating(),
                pin.getIconType()
        );
    }
}