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
    public MapResponse getMapInfo(Long chatRoomId) {
        // To Do: userchatroom에서 user id 비교해서 채팅방에 참여한 유저인지 확인 로직
        MapBoard mapBoard = mapRepository.findWithPinsByChatRoomId(chatRoomId)
                .orElseThrow(() -> new IllegalArgumentException("Map not found for chatRoomId: " + chatRoomId));
        return MapResponse.from(mapBoard);
    }

    @Transactional
    public void savePin(Long mapId, MapPinRequest request) {
        // To Do : STAR로 받을 때, HEART로 이미 존재하면 STAR로 바꾸기
        // 이미 STAR로 존재한다면 스케쥴 변경하기
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
