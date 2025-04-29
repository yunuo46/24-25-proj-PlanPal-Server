package com.gdg.planpal.domain.map.application;

import com.gdg.planpal.domain.map.dao.MapPinRepository;
import com.gdg.planpal.domain.map.dao.MapRepository;
import com.gdg.planpal.domain.map.domain.Coordinates;
import com.gdg.planpal.domain.map.domain.Map;
import com.gdg.planpal.domain.map.domain.MapPin;
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

    @Transactional(readOnly = true)
    public MapResponse getMapByChatRoom(Long chatRoomId) {
        Map map = mapRepository.findWithPinsByChatRoomId(chatRoomId)
                .orElseThrow(() -> new IllegalArgumentException("Map not found for chatRoomId: " + chatRoomId));
        return MapResponse.from(map);
    }

    @Transactional
    public void savePin(Long mapId, MapPinRequest request) {
        Map map = mapRepository.findById(mapId)
                .orElseThrow(() -> new IllegalArgumentException("Map not found with id: " + mapId));

        MapPin pin = MapPin.builder()
                .map(map)
                .coordinates(new Coordinates(request.lat(), request.lng()))
                .content(request.content())
                .iconType(request.iconType())
                .build();

        mapPinRepository.save(pin);
    }

    @Transactional
    public void deletePin(Long mapId, Long pinId) {
        MapPin pin = mapPinRepository.findById(pinId)
                .orElseThrow(() -> new IllegalArgumentException("MapPin not found with id: " + pinId));

        if (!pin.getMap().getId().equals(mapId)) {
            throw new IllegalArgumentException("MapPin does not belong to the specified Map");
        }

        mapPinRepository.delete(pin);
    }
}
