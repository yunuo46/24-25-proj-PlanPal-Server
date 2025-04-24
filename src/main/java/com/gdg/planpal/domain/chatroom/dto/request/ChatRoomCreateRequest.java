package com.gdg.planpal.domain.chatroom.dto.request;

import lombok.Getter;

@Getter
public class ChatRoomCreateRequest {
    private String name;
    private Integer limitUsers;
    private String destination;     // optional
    private String thumbnailUrl;    // optional
}
