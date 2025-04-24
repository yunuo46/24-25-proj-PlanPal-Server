package com.gdg.planpal.domain.chatroom.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ChatRoomUpdateRequest {
    private String name;
    private String destination;
    private String thumbnailUrl;
}