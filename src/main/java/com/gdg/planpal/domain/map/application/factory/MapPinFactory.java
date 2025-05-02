package com.gdg.planpal.domain.map.application.factory;

import com.gdg.planpal.domain.map.domain.IconType;
import com.gdg.planpal.domain.map.domain.MapBoard;
import com.gdg.planpal.domain.map.domain.pin.MapPin;
import com.gdg.planpal.domain.map.dto.request.MapPinRequest;
import com.gdg.planpal.domain.map.dto.request.ScheduleRequest;

public interface MapPinFactory {
    IconType mapPinType();
    MapPin create(MapBoard mapBoard, MapPinRequest request);
    MapPin addSchedule(MapPin pin, ScheduleRequest request);
}
