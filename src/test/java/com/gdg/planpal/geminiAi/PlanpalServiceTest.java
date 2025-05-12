package com.gdg.planpal.geminiAi;

import com.gdg.planpal.domain.gemini.GeminiRestService;
import com.gdg.planpal.domain.gemini.PlanPalService;
import com.gdg.planpal.domain.gemini.functionCall.Spot.AddSpotList;
import com.gdg.planpal.domain.gemini.functionCall.Spot.SpotListRepo;
import com.gdg.planpal.domain.gemini.functionCall.schedule.AddSchedule;
import com.gdg.planpal.domain.gemini.functionCall.schedule.ScheduleRepo;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.reactive.function.client.WebClient;

import java.lang.reflect.Field;


public class PlanpalServiceTest {

    private static final Logger log = LoggerFactory.getLogger(PlanpalServiceTest.class);


    @Test
    @DisplayName("gemini 테스트")
    public void testChat() throws Exception{

        GeminiRestService geminiRestService = new GeminiRestService(WebClient.builder());

        // Reflection으로 private 필드 직접 주입
        Field field = GeminiRestService.class.getDeclaredField("projectId");
        field.setAccessible(true);
        field.set(geminiRestService, "gemini-ai-455106");

        // 나머지 의존성도 생성
        SpotListRepo spotListRepo = new SpotListRepo();
        AddSpotList addSpotList = new AddSpotList();
        ScheduleRepo scheduleRepo = new ScheduleRepo();
        AddSchedule addSchedule = new AddSchedule();

        PlanPalService planPalService = new PlanPalService(
                spotListRepo, addSpotList, geminiRestService, scheduleRepo, addSchedule
        );

        Field field1 = PlanPalService.class.getDeclaredField("location");
        field1.setAccessible(true);
        field1.set(planPalService, "us-central1");
        Field field2 = PlanPalService.class.getDeclaredField("projectId");
        field2.setAccessible(true);
        field2.set(planPalService, "gemini-ai-455106");

        Field field3 = PlanPalService.class.getDeclaredField("modelId");
        field3.setAccessible(true);
        field3.set(planPalService, "gemini-2.0-flash");

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
