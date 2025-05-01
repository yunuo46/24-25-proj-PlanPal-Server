package com.gdg.planpal.domain.map.domain.pin.factory;

import com.gdg.planpal.domain.map.domain.IconType;
import com.gdg.planpal.domain.map.domain.MapBoard;
import com.gdg.planpal.domain.map.domain.pin.MapPin;
import com.gdg.planpal.domain.map.dto.request.MapPinRequest;

public interface MapPinFactory {
    IconType mapPinType();
    MapPin create(MapBoard mapBoard, MapPinRequest request);
}
