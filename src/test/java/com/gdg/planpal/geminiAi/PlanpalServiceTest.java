package com.gdg.planpal.geminiAi;

import com.gdg.planpal.domain.gemini.PlanPalService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

public class PlanpalTest {

    private static final Logger log = LoggerFactory.getLogger(PlanpalTest.class);

    @Autowired
    private PlanPalService planPalService;

    @Test
    public void testChat() {
        // given
        String prompt = "시드니 여행지와 코스를 추천해줘";

        // when
        try {
            String result = planPalService.chat(prompt);
            log.info("AI 응답: " + result);

            // 에러 메시지나 비어있는 결과 체크
            Assertions.assertFalse(
                    result == null || result.toLowerCase().contains("error"),
                    "결과가 null이거나 에러 메시지를 포함함"
            );

        } catch (Exception e) {
            Assertions.fail("chat() 실행 중 예외 발생: " + e.getMessage());
        }
    }


}
