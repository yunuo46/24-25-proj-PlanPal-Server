package com.gdg.planpal.domain.map.application;

import com.gdg.planpal.domain.map.dao.MapPinRepository;
import com.gdg.planpal.domain.map.dao.MapRepository;
import com.gdg.planpal.domain.map.domain.MapBoard;
import com.gdg.planpal.domain.map.domain.pin.MapPin;
import com.gdg.planpal.domain.map.application.factory.MapPinFactoryRouter;
import com.gdg.planpal.domain.map.dto.request.MapPinRequest;
import com.gdg.planpal.domain.map.dto.response.MapResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MapService {
    private final MapRepository mapRepository;
    private final MapPinRepository mapPinRepository;
    private final MapPinFactoryRouter mapPinFactoryRouter;

    @Transactional(readOnly = true)
    public MapResponse getMapByChatRoom(Long chatRoomId) {
        MapBoard mapBoard = mapRepository.findWithPinsByChatRoomId(chatRoomId)
                .orElseThrow(() -> new IllegalArgumentException("Map not found for chatRoomId: " + chatRoomId));
        return MapResponse.from(mapBoard);
    }

    @Transactional
    public void savePin(Long mapId, MapPinRequest request) {
        MapBoard mapBoard = mapRepository.findById(mapId)
                .orElseThrow(() -> new IllegalArgumentException("Map not found with id: " + mapId));

        MapPin pin = mapPinFactoryRouter.create(mapBoard, request);
        mapPinRepository.save(pin);
    }

    @Transactional
    public void deletePin(Long mapId, Long pinId) {
        MapPin pin = mapPinRepository.findById(pinId)
                .orElseThrow(() -> new IllegalArgumentException("MapPin not found with id: " + pinId));

        if (!pin.getMapBoard().getId().equals(mapId)) {
            throw new IllegalArgumentException("MapPin does not belong to the specified Map");
        }

        mapPinRepository.delete(pin);
    }
}
