package com.gdg.planpal.domain.map.dto.response;


import com.gdg.planpal.domain.map.domain.Map;
import com.gdg.planpal.domain.map.domain.MapPin;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public record MapResponse(
        Long id,
        Long chatRoomId,
        Double centerLat,
        Double centerLng,
        Integer zoom,
        String mapId,
        List<MapPinResponse> pins,
        LocalDateTime createdAt
) {
    public static MapResponse from(Map map) {
        return new MapResponse(
                map.getId(),
                map.getChatRoom().getId(),
                map.getCentorCoordinates().getLat(),
                map.getCentorCoordinates().getLng(),
                map.getZoom(),
                map.getMapId(),
                map.getPins().stream().map(MapPinResponse::from).collect(Collectors.toList()),
                map.getCreatedAt()
        );
    }
}

