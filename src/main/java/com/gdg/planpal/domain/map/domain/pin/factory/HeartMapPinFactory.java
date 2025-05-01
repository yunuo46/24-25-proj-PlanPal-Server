package com.gdg.planpal.domain.map.domain.pin.factory;

import com.gdg.planpal.domain.map.domain.Coordinates;
import com.gdg.planpal.domain.map.domain.IconType;
import com.gdg.planpal.domain.map.domain.MapBoard;
import com.gdg.planpal.domain.map.domain.pin.HeartMapPin;
import com.gdg.planpal.domain.map.domain.pin.MapPin;
import com.gdg.planpal.domain.map.dto.request.MapPinRequest;
import org.springframework.stereotype.Component;

@Component
public class HeartMapPinFactory implements MapPinFactory{
    @Override
    public IconType mapPinType() {
        return IconType.HEART;
    }

    @Override
    public MapPin create(MapBoard mapBoard, MapPinRequest request) {
        return HeartMapPin.builder()
                .mapBoard(mapBoard)
                .coordinates(new Coordinates(request.lat(), request.lng()))
                .placeId(request.placeId())
                .content(request.content())
                .build();
    }
}
