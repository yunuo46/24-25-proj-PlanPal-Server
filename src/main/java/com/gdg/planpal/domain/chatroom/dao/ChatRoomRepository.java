package com.gdg.planpal.domain.chatroom.dao;

import com.gdg.planpal.domain.chatroom.domain.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {
    Optional<ChatRoom> findByInviteCode(String inviteCode);
}
