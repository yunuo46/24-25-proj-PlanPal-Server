package com.gdg.planpal.domain.map.application.factory;

import com.gdg.planpal.domain.map.domain.IconType;
import com.gdg.planpal.domain.map.domain.MapBoard;
import com.gdg.planpal.domain.map.domain.pin.MapPin;
import com.gdg.planpal.domain.map.dto.request.MapPinRequest;
import com.gdg.planpal.domain.map.dto.request.ScheduleRequest;
import com.gdg.planpal.domain.user.domain.User;

public interface MapPinFactory {
    IconType mapPinType();
    MapPin save(MapBoard mapBoard, MapPinRequest request, User user);
}
