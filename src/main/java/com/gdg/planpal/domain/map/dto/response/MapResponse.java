package com.gdg.planpal.domain.map.dto.response;


import com.gdg.planpal.domain.map.domain.MapBoard;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public record MapResponse(
        Long id,
        Long chatRoomId,
        Double centerLat,
        Double centerLng,
        List<MapPinResponse> pins,
        LocalDateTime createdAt
) {
    public static MapResponse from(MapBoard mapBoard) {
        return new MapResponse(
                mapBoard.getId(),
                mapBoard.getChatRoom().getId(),
                mapBoard.getCentorCoordinates().getLat(),
                mapBoard.getCentorCoordinates().getLng(),
                mapBoard.getPins() == null
                        ? List.of()
                        : mapBoard.getPins().stream().map(MapPinResponse::from).collect(Collectors.toList()),
                mapBoard.getCreatedAt()
        );
    }
}

