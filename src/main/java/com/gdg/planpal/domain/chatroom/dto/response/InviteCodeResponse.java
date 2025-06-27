package com.gdg.planpal.domain.chatroom.dto.response;

import jakarta.validation.constraints.NotNull;

public record InviteCodeResponse(
        @NotNull(message = "초대 코드는 필수 응답입니다.") String inviteCode
) {}
