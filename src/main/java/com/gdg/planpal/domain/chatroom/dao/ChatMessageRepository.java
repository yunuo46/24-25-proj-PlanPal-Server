package com.gdg.planpal.domain.chatroom.dao;

import com.gdg.planpal.domain.chatroom.domain.ChatMessage;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;

public interface ChatMessageRepository extends ReactiveMongoRepository<ChatMessage, String> {
    Flux<ChatMessage> findByRoomId(String roomId);
}
