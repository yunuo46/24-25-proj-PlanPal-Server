package com.gdg.planpal.domain.chatroom.dto.response;

import com.gdg.planpal.domain.chatroom.domain.ChatRoom;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ChatRoomSummaryResponse {
    private Long id;
    private String name;
    private Integer limitUsers;
    private String destination;
    private String thumbnailUrl;

    public static ChatRoomSummaryResponse from(ChatRoom room) {
        return ChatRoomSummaryResponse.builder()
                .id(room.getId())
                .name(room.getName())
                .limitUsers(room.getLimitUsers())
                .destination(room.getDestination())
                .thumbnailUrl(room.getThumbnailUrl())
                .build();
    }
}
