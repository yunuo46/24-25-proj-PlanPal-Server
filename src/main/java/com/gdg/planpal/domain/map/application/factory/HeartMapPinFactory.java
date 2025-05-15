package com.gdg.planpal.domain.map.application.factory;

import com.gdg.planpal.domain.map.domain.Coordinates;
import com.gdg.planpal.domain.map.domain.IconType;
import com.gdg.planpal.domain.map.domain.MapBoard;
import com.gdg.planpal.domain.map.domain.pin.HeartMapPin;
import com.gdg.planpal.domain.map.domain.pin.MapPin;
import com.gdg.planpal.domain.map.domain.pin.StarMapPin;
import com.gdg.planpal.domain.schedule.domain.StarMapPinSchedule;
import com.gdg.planpal.domain.map.dto.request.MapPinRequest;
import com.gdg.planpal.domain.map.dto.request.ScheduleRequest;
import com.gdg.planpal.domain.user.domain.User;
import org.springframework.stereotype.Component;

@Component
public class HeartMapPinFactory implements MapPinFactory{
    @Override
    public IconType mapPinType() {
        return IconType.HEART;
    }

    @Override
    public MapPin create(MapBoard mapBoard, MapPinRequest request, User user) {
        return HeartMapPin.builder()
                .mapBoard(mapBoard)
                .user(user)
                .coordinates(new Coordinates(request.lat(), request.lng()))
                .placeId(request.placeId())
                .title(request.title())
                .address(request.address())
                .content(request.content())
                .type(request.type())
                .rating(request.rating())
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
