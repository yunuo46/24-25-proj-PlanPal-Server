package com.gdg.planpal.domain.map.application.factory;

import com.gdg.planpal.domain.map.domain.IconType;
import com.gdg.planpal.domain.map.domain.MapBoard;
import com.gdg.planpal.domain.map.domain.pin.MapPin;
import com.gdg.planpal.domain.map.dto.request.MapPinRequest;

import com.gdg.planpal.domain.map.dto.request.ScheduleRequest;
import com.gdg.planpal.domain.user.domain.User;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class MapPinFactoryRouter {
    private final Map<IconType, MapPinFactory> factories;

    public MapPinFactoryRouter(List<MapPinFactory> factories) {
        this.factories = factories.stream()
                .collect(Collectors.toUnmodifiableMap(
                        MapPinFactory::mapPinType,
                        factory -> factory
                ));
    }

    public MapPin create(MapBoard mapBoard, MapPinRequest request, User user) {
        MapPinFactory factory = factories.get(request.iconType());
        return factory.create(mapBoard, request, user);
    }

    public MapPin addSchedule(MapPin pin, ScheduleRequest request) {
        MapPinFactory factory = factories.get(pin.getIconType());
        return factory.addSchedule(pin, request);
    }
}