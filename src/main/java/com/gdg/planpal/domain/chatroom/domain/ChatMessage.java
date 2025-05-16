package com.gdg.planpal.domain.chatroom.domain;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "chat_messages")
public class ChatMessage {

    @Id
    private String id;

    private String type;
    private String roomId;
    private String imgUrl;
    private String senderName;
    private String text;
    private String senderSessionId;
    @Builder.Default
    private Long timestamp = System.currentTimeMillis();
}
