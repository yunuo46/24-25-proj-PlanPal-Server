package com.gdg.planpal.domain.chatroom.dto.request;

import jakarta.validation.constraints.NotNull;

public record ChatRoomJoinRequest(
        @NotNull(message = "초대 코드는 필수 항목입니다.") String inviteCode
) {}
