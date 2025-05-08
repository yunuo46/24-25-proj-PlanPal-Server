package com.gdg.planpal.domain.chatroom.dto.request;

public record ChatRoomUpdateRequest(
        String name,
        String destination,
        String thumbnailUrl
) {}