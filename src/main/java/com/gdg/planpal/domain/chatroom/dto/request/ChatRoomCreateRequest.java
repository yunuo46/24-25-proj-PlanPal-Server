package com.gdg.planpal.domain.chatroom.dto.request;

import com.gdg.planpal.domain.map.domain.Coordinates;
import jakarta.validation.constraints.NotNull;

public record ChatRoomCreateRequest(
        @NotNull(message = "채팅방 이름은 필수 항목입니다.") String name,
        @NotNull(message = "최대 인원 수는 필수 항목입니다.") Integer limitUsers,
        @NotNull(message = "좌표는 필수 항목입니다.") Coordinates coordinates,
        String destination,
        String thumbnailUrl
) {}