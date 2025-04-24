package com.gdg.planpal.domain.chatroom.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ChatRoomJoinRequest {
    private String inviteCode;
}
