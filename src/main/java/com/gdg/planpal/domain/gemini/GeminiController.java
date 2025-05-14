package com.gdg.planpal.domain.gemini;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
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

    @GetMapping("/plan-pal")
    public String planPal_chat(@RequestParam Long chatRoomId,@RequestParam String prompt) {
        return planPalService.chat(chatRoomId,prompt);
    }

    public Mono<ResponseEntity<String>> groundedQuery(@RequestParam String prompt) {
        return geminiRestService.chatWithDynamicSearch(prompt,0.1)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.noContent().build());
    }

}
