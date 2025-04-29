package com.gdg.planpal.domain.map.dao;

import com.gdg.planpal.domain.map.domain.Map;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface MapRepository extends JpaRepository<Map, Long> {
    @Query("SELECT m FROM Map m LEFT JOIN FETCH m.pins WHERE m.chatRoom.id = :chatRoomId")
    Optional<Map> findWithPinsByChatRoomId(@Param("chatRoomId") Long chatRoomId);
}
