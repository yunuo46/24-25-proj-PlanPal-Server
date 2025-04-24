package com.gdg.planpal.domain.chatroom.api;

import com.gdg.planpal.domain.chatroom.application.ChatRoomService;
import com.gdg.planpal.domain.chatroom.dto.request.ChatRoomCreateRequest;
import com.gdg.planpal.domain.chatroom.dto.request.ChatRoomUpdateRequest;
import com.gdg.planpal.domain.chatroom.dto.response.ChatRoomResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/chat-rooms")
@RequiredArgsConstructor
public class ChatRoomApi {

    private final ChatRoomService chatRoomService;

    @GetMapping
    public ResponseEntity<List<ChatRoomResponse>> list(@AuthenticationPrincipal Long userId) {
        return ResponseEntity.ok(chatRoomService.findAllByUser(userId));
    }

    @GetMapping("/{chatRoomId}/invite")
    public ResponseEntity<String> getInviteCode(@AuthenticationPrincipal Long userId,
                                                @PathVariable Long chatRoomId) {
        return ResponseEntity.ok(chatRoomService.getInviteCode(chatRoomId, userId));
    }

    @PostMapping
    public ResponseEntity<ChatRoomResponse> create(@RequestBody ChatRoomCreateRequest request, @AuthenticationPrincipal Long userId) {
        return ResponseEntity.ok(chatRoomService.create(request, userId));
    }

    @PostMapping("/join/{inviteCode}")
    public ResponseEntity<ChatRoomResponse> join(@AuthenticationPrincipal Long userId,
                                                 @PathVariable String inviteCode) {
        return ResponseEntity.ok(chatRoomService.joinChatRoom(inviteCode, userId));
    }

    @DeleteMapping("/{chatRoomId}")
    public ResponseEntity<Void> delete(@AuthenticationPrincipal Long userId,
                                       @PathVariable Long chatRoomId) {
        chatRoomService.deleteChatRoom(chatRoomId, userId);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{chatRoomId}")
    public ResponseEntity<ChatRoomResponse> update(@AuthenticationPrincipal Long userId,
                                                   @PathVariable Long chatRoomId,
                                                   @RequestBody ChatRoomUpdateRequest request) {
        return ResponseEntity.ok(chatRoomService.updateChatRoom(chatRoomId, userId, request));
    }

}
