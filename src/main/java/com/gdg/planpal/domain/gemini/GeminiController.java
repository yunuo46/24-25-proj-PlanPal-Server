package com.gdg.planpal.domain.gemini;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
@RequestMapping("/ai")
public class GeminiController {
    private final GeminiRestService geminiRestService;
    private final PlanPalService planPalService;

    @GetMapping("/ai-message")
    public String planPal_chat(@RequestParam Long chatRoomId,@RequestParam String prompt, @AuthenticationPrincipal String userName) {
        return planPalService.chat(userName,chatRoomId,prompt);
    }


}
