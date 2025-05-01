package com.gdg.planpal.domain.map.application.factory;

import com.gdg.planpal.domain.map.domain.Coordinates;
import com.gdg.planpal.domain.map.domain.IconType;
import com.gdg.planpal.domain.map.domain.MapBoard;
import com.gdg.planpal.domain.map.domain.pin.MapPin;
import com.gdg.planpal.domain.map.domain.pin.StarMapPin;
import com.gdg.planpal.domain.map.domain.pin.StarMapPinSchedule;
import com.gdg.planpal.domain.map.dto.request.MapPinRequest;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class StarMapPinFactory implements MapPinFactory{
    @Override
    public IconType mapPinType() {
        return IconType.STAR;
    }

    @Override
    public MapPin create(MapBoard mapBoard, MapPinRequest request) {
        StarMapPin pin = StarMapPin.builder()
                .mapBoard(mapBoard)
                .coordinates(new Coordinates(request.lat(), request.lng()))
                .placeId(request.placeId())
                .content(request.content())
                .build();

        List<StarMapPinSchedule> schedules = Optional.ofNullable(request.schedules())
                .orElse(List.of())
                .stream()
                .map(r -> r.from(pin))
                .toList();

        pin.getSchedules().addAll(schedules);
        return pin;
    }
}
