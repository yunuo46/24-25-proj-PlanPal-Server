package com.gdg.planpal.domain.chatroom.dto.response;

import com.gdg.planpal.domain.chatroom.domain.ChatRoom;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.format.DateTimeFormatter;

@Getter
@Builder
@AllArgsConstructor
public class ChatRoomResponse {
    private Long id;
    private String name;
    private String inviteCode;
    private Integer limitUsers;
    private String destination;
    private String thumbnailUrl;
    private String createdAt;

    public static ChatRoomResponse from(ChatRoom room) {
        return ChatRoomResponse.builder()
                .id(room.getId())
                .name(room.getName())
                .inviteCode(room.getInviteCode())
                .limitUsers(room.getLimitUsers())
                .destination(room.getDestination())
                .thumbnailUrl(room.getThumbnailUrl())
                .createdAt(room.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")))
                .build();
    }
}
