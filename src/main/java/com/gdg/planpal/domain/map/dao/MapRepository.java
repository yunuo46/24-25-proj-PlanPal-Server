package com.gdg.planpal.domain.map.dao;

import com.gdg.planpal.domain.map.domain.MapBoard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface MapRepository extends JpaRepository<MapBoard, Long> {
    @Query("""
       SELECT m FROM MapBoard m
       JOIN FETCH m.pins
       WHERE m.chatRoom.id = :chatRoomId
    """)
    Optional<MapBoard> findWithPinsByChatRoomId(@Param("chatRoomId") Long chatRoomId);
}
