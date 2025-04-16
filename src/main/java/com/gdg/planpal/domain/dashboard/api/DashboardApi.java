package com.gdg.planpal.domain.dashboard.api;

import com.gdg.planpal.domain.user.domain.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardApi {
    // 채팅방 생성 (POST)
    @PostMapping
    public ResponseEntity<String> createChatRoom(@AuthenticationPrincipal Long userId) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body("Chat room '" + userId + "' created (mock)");
    }
}
