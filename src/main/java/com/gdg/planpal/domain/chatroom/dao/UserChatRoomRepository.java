package com.gdg.planpal.domain.chatroom.dao;

import com.gdg.planpal.domain.chatroom.domain.UserChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserChatRoomRepository extends JpaRepository<UserChatRoom, Long> {
    List<UserChatRoom> findAllByUserId(Long userId);

    boolean existsByUserIdAndChatRoomId(Long userId, Long id);

    void deleteByUserIdAndChatRoomId(Long userId, Long chatRoomId);
}
