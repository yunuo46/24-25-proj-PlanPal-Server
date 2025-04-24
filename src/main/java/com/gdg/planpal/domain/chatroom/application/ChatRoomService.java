package com.gdg.planpal.domain.chatroom.application;

import com.gdg.planpal.domain.chatroom.dao.ChatRoomRepository;
import com.gdg.planpal.domain.chatroom.dao.UserChatRoomRepository;
import com.gdg.planpal.domain.chatroom.domain.ChatRoom;
import com.gdg.planpal.domain.chatroom.domain.UserChatRoom;
import com.gdg.planpal.domain.chatroom.dto.request.ChatRoomCreateRequest;
import com.gdg.planpal.domain.chatroom.dto.request.ChatRoomUpdateRequest;
import com.gdg.planpal.domain.chatroom.dto.response.ChatRoomResponse;
import com.gdg.planpal.domain.user.dao.UserRepository;
import com.gdg.planpal.domain.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChatRoomService {

    private final ChatRoomRepository chatRoomRepository;
    private final UserChatRoomRepository userChatRoomRepository;
    private final UserRepository userRepository;

    @Transactional
    public ChatRoomResponse create(ChatRoomCreateRequest request, Long userId) {
        User owner = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("유저를 찾을 수 없습니다."));

        ChatRoom chatRoom = ChatRoom.builder()
                .name(request.getName())
                .limitUsers(request.getLimitUsers())
                .inviteCode(generateInviteCode())
                .destination(request.getDestination())
                .thumbnailUrl(request.getThumbnailUrl())
                .owner(owner)
                .build();

        chatRoomRepository.save(chatRoom);

        UserChatRoom participation = UserChatRoom.builder()
                .user(owner)
                .chatRoom(chatRoom)
                .build();

        userChatRoomRepository.save(participation); // 생성 시 소유자는 자동 참여
        return ChatRoomResponse.from(chatRoom);
    }

    @Transactional(readOnly = true)
    public String getInviteCode(Long chatRoomId, Long userId) {
        if (!userChatRoomRepository.existsByUserIdAndChatRoomId(userId, chatRoomId)) {
            throw new SecurityException("채팅방에 참여 중인 유저만 초대 코드를 조회할 수 있습니다.");
        }
        return chatRoomRepository.findById(chatRoomId)
                .map(ChatRoom::getInviteCode)
                .orElseThrow(() -> new IllegalArgumentException("채팅방이 존재하지 않습니다."));
    }

    @Transactional(readOnly = true)
    public List<ChatRoomResponse> findAllByUser(Long userId) {
        return userChatRoomRepository.findAllByUserId(userId).stream()
                .map(uc -> ChatRoomResponse.from(uc.getChatRoom()))
                .collect(Collectors.toList());
    }

    private String generateInviteCode() {
        return UUID.randomUUID().toString().substring(0, 8);
    }

    @Transactional
    public ChatRoomResponse joinChatRoom(String inviteCode, Long userId) {
        ChatRoom chatRoom = chatRoomRepository.findByInviteCode(inviteCode)
                .orElseThrow(() -> new IllegalArgumentException("잘못된 초대 코드입니다."));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("유저를 찾을 수 없습니다."));

        boolean alreadyJoined = userChatRoomRepository.existsByUserIdAndChatRoomId(userId, chatRoom.getId());
        if (!alreadyJoined) {
            UserChatRoom userChatRoom = UserChatRoom.builder()
                    .user(user)
                    .chatRoom(chatRoom)
                    .build();
            userChatRoomRepository.save(userChatRoom);
        }
        return ChatRoomResponse.from(chatRoom);
    }

    @Transactional
    public void deleteChatRoom(Long chatRoomId, Long userId) {
        ChatRoom room = chatRoomRepository.findById(chatRoomId)
                .orElseThrow(() -> new IllegalArgumentException("채팅방이 존재하지 않습니다."));

        if (room.getOwner().getId().equals(userId)) {
            chatRoomRepository.delete(room); // 방 주인이면 삭제
        } else {
            userChatRoomRepository.deleteByUserIdAndChatRoomId(userId, chatRoomId); // 참여자는 나가기
        }
    }

    @Transactional
    public ChatRoomResponse updateChatRoom(Long chatRoomId, Long userId, ChatRoomUpdateRequest request) {
        ChatRoom room = chatRoomRepository.findById(chatRoomId)
                .orElseThrow(() -> new IllegalArgumentException("채팅방이 존재하지 않습니다."));

        if (!room.getOwner().getId().equals(userId)) {
            throw new SecurityException("채팅방의 소유자만 수정할 수 있습니다.");
        }

        try {
            room.updateInfo(request.getName(), request.getDestination(), request.getThumbnailUrl());
        } catch (Exception e) {
            throw new IllegalStateException("채팅방 이름이 존재하지 않습니다.", e);
        }
        return ChatRoomResponse.from(room);
    }
}
