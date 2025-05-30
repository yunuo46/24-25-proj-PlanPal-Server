package com.gdg.planpal.domain.map.api;

import com.gdg.planpal.domain.map.application.MapService;
import com.gdg.planpal.domain.map.dto.request.MapPinRequest;
import com.gdg.planpal.domain.map.dto.response.MapResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/maps")
@SecurityRequirement(name = "accessToken")
public class MapApi {
    private final MapService mapService;

    @GetMapping("/chat-rooms/{chatRoomId}")
    @Operation(summary = "맵 정보 조회", description = "채팅방 ID로 해당 맵 정보를 조회합니다.")
    public ResponseEntity<MapResponse> getMapInfo(@PathVariable Long chatRoomId) {
        return ResponseEntity.ok(mapService.getMapInfo(chatRoomId));
    }

    @PostMapping("/{chatroomId}/pins")
    @Operation(summary = "핀 저장 및 스케쥴 추가", description = "지도에 핀을 추가합니다. 요청에 스케쥴이 존재한다면, 스케쥴을 추가합니다.")
    public ResponseEntity<Void> savePin(@PathVariable Long chatroomId, @RequestBody MapPinRequest request, @AuthenticationPrincipal Long userId) {
        mapService.savePin(chatroomId, request, userId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{chatroomId}/pins/{placeId}")
    @Operation(summary = "핀 삭제", description = "지도에 저장된 핀을 삭제합니다.")
    public ResponseEntity<Void> deletePin(@PathVariable Long chatroomId, @PathVariable String placeId) {
        mapService.deletePin(chatroomId, placeId);
        return ResponseEntity.noContent().build();
    }
}
