package com.gdg.planpal.domain.chatroom.api;

import com.gdg.planpal.domain.chatroom.application.ChatRoomService;
import com.gdg.planpal.domain.chatroom.dto.request.ChatRoomCreateRequest;
import com.gdg.planpal.domain.chatroom.dto.request.ChatRoomJoinRequest;
import com.gdg.planpal.domain.chatroom.dto.request.ChatRoomUpdateRequest;
import com.gdg.planpal.domain.chatroom.dto.response.ChatRoomResponse;
import com.gdg.planpal.domain.chatroom.dto.response.ChatRoomSummaryResponse;
import com.gdg.planpal.domain.chatroom.dto.response.InviteCodeResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/chat-rooms")
@RequiredArgsConstructor
@SecurityRequirement(name = "accessToken")
public class ChatRoomApi {

    private final ChatRoomService chatRoomService;

    @GetMapping
    @Operation(summary = "채팅방 요약 목록 조회", description = "유저가 참여 중인 채팅방들의 요약 정보를 조회합니다.")
    public ResponseEntity<List<ChatRoomSummaryResponse>> list(@AuthenticationPrincipal Long userId) {
        System.out.println("userId = " + userId);
        return ResponseEntity.ok(chatRoomService.getAllSummariesByUser(userId));
    }

    @PostMapping
    @Operation(summary = "채팅방 생성", description = "채팅방을 생성하고 자신이 소유자가 됩니다.")
    public ResponseEntity<ChatRoomResponse> create(@RequestBody ChatRoomCreateRequest request,
                                                   @AuthenticationPrincipal Long userId) {
        return ResponseEntity.ok(chatRoomService.create(request, userId));
    }

    @PostMapping("/join")
    @Operation(summary = "채팅방 참여", description = "초대 코드를 통해 채팅방에 참여합니다.")
    public ResponseEntity<ChatRoomResponse> join(@AuthenticationPrincipal Long userId,
                                                 @RequestBody ChatRoomJoinRequest request) {
        return ResponseEntity.ok(chatRoomService.joinChatRoom(request, userId));
    }

    @GetMapping("/{chatRoomId}")
    @Operation(summary = "채팅방 상세 정보 조회", description = "채팅방의 상세 정보를 조회합니다.")
    public ResponseEntity<ChatRoomResponse> getInfo(@AuthenticationPrincipal Long userId,
                                                    @PathVariable Long chatRoomId) {
        return ResponseEntity.ok(chatRoomService.getChatRoom(chatRoomId, userId));
    }

    @DeleteMapping("/{chatRoomId}")
    @Operation(summary = "채팅방 삭제 또는 나가기", description = "채팅방 주인은 삭제, 참여자는 나가기 처리합니다.")
    public ResponseEntity<Void> delete(@AuthenticationPrincipal Long userId,
                                       @PathVariable Long chatRoomId) {
        chatRoomService.deleteChatRoom(chatRoomId, userId);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{chatRoomId}")
    @Operation(summary = "채팅방 정보 수정", description = "채팅방 이름, 목적지, 대표 사진을 수정합니다.")
    public ResponseEntity<ChatRoomResponse> update(@AuthenticationPrincipal Long userId,
                                                   @PathVariable Long chatRoomId,
                                                   @RequestBody ChatRoomUpdateRequest request) {
        return ResponseEntity.ok(chatRoomService.updateChatRoom(chatRoomId, userId, request));
    }

    @GetMapping("/{chatRoomId}/invite")
    @Operation(summary = "채팅방 초대 코드 조회", description = "참여 중인 채팅방의 초대 코드를 조회합니다.")
    public ResponseEntity<InviteCodeResponse> getInviteCode(@AuthenticationPrincipal Long userId,
                                                            @PathVariable Long chatRoomId) {
        return ResponseEntity.ok(chatRoomService.getInviteCode(chatRoomId, userId));
    }
}
