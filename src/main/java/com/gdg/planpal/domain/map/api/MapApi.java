package com.gdg.planpal.domain.map.api;

import com.gdg.planpal.domain.map.application.MapService;
import com.gdg.planpal.domain.map.dto.request.MapPinRequest;
import com.gdg.planpal.domain.map.dto.response.MapResponse;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/map")
public class MapApi {
    private final MapService mapService;

    @GetMapping("/{chatRoomId}")
    @Operation(summary = "맵 정보 조회", description = "채팅방 ID로 해당 맵 정보를 조회합니다.")
    public ResponseEntity<MapResponse> getMapInfo(@PathVariable Long chatRoomId) {
        return ResponseEntity.ok(mapService.getMapInfo(chatRoomId));
    }

    @PostMapping("/{mapId}/pins")
    @Operation(summary = "핀 저장", description = "지도에 핀을 추가합니다.")
    public ResponseEntity<Void> savePin(@PathVariable Long mapId, @RequestBody MapPinRequest request) {
        mapService.savePin(mapId, request);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{mapId}/pins/{pinId}")
    @Operation(summary = "핀 삭제", description = "지도에 저장된 핀을 삭제합니다.")
    public ResponseEntity<Void> deletePin(@PathVariable Long mapId, @PathVariable Long pinId) {
        mapService.deletePin(mapId, pinId);
        return ResponseEntity.noContent().build();
    }
}
