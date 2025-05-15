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

import java.util.List;
import java.util.Optional;

@Component
public class StarMapPinFactory implements MapPinFactory{
    @Override
    public IconType mapPinType() {
        return IconType.STAR;
    }

    @Override
    public MapPin save(MapBoard mapBoard, MapPinRequest request, User user) {
        Optional<MapPin> existingPinOpt = mapBoard.getPins().stream()
                .filter(pin -> pin.getPlaceId().equals(request.placeId()))
                .findFirst();

        MapPin existingPin = existingPinOpt.orElseThrow(() ->
                new IllegalStateException("기존 핀 정보가 존재하지 않습니다. 존재하는 핀에 스케쥴을 추가하는 경우에만 호출되어야 합니다."));

        if(existingPin instanceof HeartMapPin heartMapPin) {
            // 부모와의 연관관계를 제거하여 고아 객체로 취급되어 삭제
            mapBoard.getPins().remove(heartMapPin);
            user.getMapPins().remove(heartMapPin);

            StarMapPin newPin = StarMapPin.builder()
                    .mapBoard(mapBoard)
                    .user(user)
                    .placeId(request.placeId())
                    .title(request.title())
                    .address(request.address())
                    .content(request.content())
                    .type(request.type())
                    .rating(request.rating())
                    .build();

            StarMapPinSchedule schedule = request.schedule().from(newPin);
            newPin.getSchedules().add(schedule);
            return newPin;
        }else if (existingPin instanceof StarMapPin starPin) {
            // 기존 star에 스케줄만 추가
            StarMapPinSchedule schedule = request.schedule().from(starPin);
            starPin.getSchedules().add(schedule);
            return starPin;
        }else {
            throw new IllegalStateException("HEART 또는 STAR 핀만 허용됩니다.");
        }
    }
}
