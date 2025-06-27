package com.gdg.planpal.domain.chatroom.dto.response;

import com.gdg.planpal.domain.chatroom.domain.ChatMessage;
import jakarta.validation.constraints.NotNull;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public record ChatMessageResponse(
        @NotNull(message = "메시지 타입은 필수입니다.") String type,
        @NotNull(message = "채팅방 ID는 필수입니다.") String roomId,
        @NotNull(message = "이미지 URL은 필수입니다.") String imgUrl,
        @NotNull(message = "보낸 사람 이름은 필수입니다.") String senderName,
        @NotNull(message = "메시지 내용은 필수입니다.") String text,
        @NotNull(message = "타임스탬프는 필수입니다.") String timestamp // <- Long → String으로 변경
) {

    public static ChatMessageResponse from(ChatMessage chatMessage) {
        String timestampKST = DateTimeFormatter.ISO_OFFSET_DATE_TIME
                .withZone(ZoneId.of("Asia/Seoul"))
                .format(Instant.ofEpochMilli(chatMessage.getTimestamp()));

        return new ChatMessageResponse(
                chatMessage.getType(),
                chatMessage.getRoomId(),
                chatMessage.getImgUrl(),
                chatMessage.getSenderName(),
                chatMessage.getText(),
                timestampKST
        );
    }
}