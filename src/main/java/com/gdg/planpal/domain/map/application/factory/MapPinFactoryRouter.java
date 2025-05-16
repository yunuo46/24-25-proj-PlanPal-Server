package com.gdg.planpal.domain.map.application.factory;

import com.gdg.planpal.domain.map.domain.IconType;
import com.gdg.planpal.domain.map.domain.MapBoard;
import com.gdg.planpal.domain.map.domain.pin.MapPin;
import com.gdg.planpal.domain.map.dto.request.MapPinRequest;
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

    public MapPin save(MapBoard mapBoard, MapPinRequest request, User user) {
        MapPinFactory factory = factories.get(request.iconType());
        return factory.save(mapBoard, request, user);
    }
}