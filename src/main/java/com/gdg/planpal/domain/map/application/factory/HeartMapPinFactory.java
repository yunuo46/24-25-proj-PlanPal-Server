package com.gdg.planpal.domain.map.application.factory;

import com.gdg.planpal.domain.map.domain.Coordinates;
import com.gdg.planpal.domain.map.domain.IconType;
import com.gdg.planpal.domain.map.domain.MapBoard;
import com.gdg.planpal.domain.map.domain.pin.HeartMapPin;
import com.gdg.planpal.domain.map.domain.pin.MapPin;
import com.gdg.planpal.domain.map.domain.pin.StarMapPin;
import com.gdg.planpal.domain.map.domain.pin.StarMapPinSchedule;
import com.gdg.planpal.domain.map.dto.request.MapPinRequest;
import com.gdg.planpal.domain.map.dto.request.ScheduleRequest;
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

    @Override
    public MapPin addSchedule(MapPin pin, ScheduleRequest request) {
        if (!(pin instanceof HeartMapPin heartPin)) {
            throw new IllegalArgumentException("Expected HeartMapPin");
        }

        StarMapPin starPin = StarMapPin.builder()
                .mapBoard(heartPin.getMapBoard())
                .coordinates(heartPin.getCoordinates())
                .placeId(heartPin.getPlaceId())
                .content(heartPin.getContent())
                .build();

        starPin.getSchedules().add(StarMapPinSchedule.builder()
                .mapPin(starPin)
                .startTime(request.startTime())
                .endTime(request.endTime())
                .build());

        return starPin;
    }
}
