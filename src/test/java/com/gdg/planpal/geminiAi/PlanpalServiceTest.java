package com.gdg.planpal.geminiAi;

import com.gdg.planpal.domain.gemini.GeminiRestService;
import com.gdg.planpal.domain.gemini.PlanPalService;
import com.gdg.planpal.domain.gemini.functionCall.Spot.AddSpotList;
import com.gdg.planpal.domain.gemini.functionCall.Spot.SpotListRepo;
import com.gdg.planpal.domain.gemini.functionCall.schedule.AddSchedule;
import com.gdg.planpal.domain.gemini.functionCall.schedule.ScheduleRepo;
import com.gdg.planpal.domain.googleMap.GoogleMapService;
import com.gdg.planpal.domain.map.application.MapService;
import com.gdg.planpal.domain.map.dao.MapPinRepository;
import com.gdg.planpal.domain.map.domain.MapBoard;
import com.gdg.planpal.domain.map.domain.pin.MapPin;
import com.gdg.planpal.domain.map.dto.request.MapPinRequest;
import com.gdg.planpal.domain.map.dto.response.MapResponse;
import com.gdg.planpal.domain.schedule.dao.StarMapPinScheduleRepository;
import com.gdg.planpal.domain.schedule.domain.StarMapPinSchedule;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
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
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import static org.mockito.Mockito.*;


public class PlanpalServiceTest {

    private static final Logger log = LoggerFactory.getLogger(PlanpalServiceTest.class);
    private MapPinRepository mockPinRepo;
    private StarMapPinScheduleRepository mockScheduleRepo;
    //private SpotListRepo spotListRepo;
    private MapService mapService;
    private GoogleMapService googleMapService;
    @BeforeEach
    void setUp() {
        // 1. MapPinRepository mock 객체 생성
        mockPinRepo = mock(MapPinRepository.class);
        mockScheduleRepo = mock(StarMapPinScheduleRepository.class);
        mapService = mock(MapService.class);

// 2. 리턴할 가짜 MapPin 객체들 생성
        MapPin pin1 = mock(MapPin.class);
        when(pin1.getId()).thenReturn(1L);
        when(pin1.getTitle()).thenReturn("Royal Botanic Gardens Melbourne");

        MapPin pin2 = mock(MapPin.class);
        when(pin2.getId()).thenReturn(2L);
        when(pin2.getTitle()).thenReturn("National Gallery of Victoria");


        MapResponse mockResponse = mock(MapResponse.class);
        when(mockResponse.id()).thenReturn(123L);
        when(mapService.getMapInfo(11L)).thenReturn(mockResponse);
        //when(mapService.savePin(100L,any(),123L)).thenReturn();
        //doNothing().when(mapService).savePin(100L,any(),123L);

        doAnswer(invocation -> {
            MapPinRequest arg = invocation.getArgument(1);
            System.out.println("savePin 호출됨. 메시지: " + arg.toString());
            return null;
        }).when(mapService).savePin(eq(100L),any(),eq(123L));

        LocalDateTime time1 = LocalDateTime.of(2025,5,7,14,30);
        LocalDateTime time2 = LocalDateTime.of(2025,5,7,17,30);
        LocalDateTime time3 = LocalDateTime.of(2025,5,7,11,00);
        LocalDateTime time4 = LocalDateTime.of(2025,5,7,13,00);

        StarMapPinSchedule schedule1 = mock(StarMapPinSchedule.class);
        String timeInfo1 = "["+pin1.getId().toString()+":"+pin1.getContent()+"]"+time1.toString()+"~"+time2.toString();
        when(schedule1.toString()).thenReturn(timeInfo1);

        String timeInfo2 = "["+pin2.getId().toString()+":"+pin2.getContent()+"]"+time3.toString()+"~"+time4.toString();
        StarMapPinSchedule schedule2 = mock(StarMapPinSchedule.class);
        when(schedule2.toString()).thenReturn(timeInfo2);


        when(mockScheduleRepo.findAllByMapId(123L)).thenReturn(List.of(schedule1));

// 3. mockRepo 가 특정 mapBoardId로 호출되었을 때 결과 지정
        when(mockPinRepo.findByMapBoardId(123L)).thenReturn(List.of(pin1, pin2));
    }
    @Test
    @DisplayName("gemini 테스트")
    public void testChat() throws Exception{
        String userId = "100";

        GeminiRestService geminiRestService = new GeminiRestService(WebClient.builder());

        // Reflection으로 private 필드 직접 주입
        Field field = GeminiRestService.class.getDeclaredField("projectId");
        field.setAccessible(true);
        field.set(geminiRestService, "gemini-ai-455106");

        googleMapService =new GoogleMapService();
        Field mapField = GoogleMapService.class.getDeclaredField("apiKey");
        mapField.setAccessible(true);
        mapField.set(googleMapService, System.getenv("GOOGLE_PLACES_API_KEY"));
        googleMapService.init();

        // 나머지 의존성도 생성
        SpotListRepo spotListRepo = new SpotListRepo(mockPinRepo);
        AddSpotList addSpotList = new AddSpotList(googleMapService,mapService);
        ScheduleRepo scheduleRepo = new ScheduleRepo(mockScheduleRepo);





        AddSchedule addSchedule = new AddSchedule();
        PlanPalService planPalService = new PlanPalService(
                spotListRepo, addSpotList, geminiRestService, scheduleRepo, addSchedule,mapService
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
        String prompt = "서핑하기 좋은 시드니 해변을 추가로 찾아서 추가해줘";
        Long chatRoomId=11L;

        /**
         * spot repo와 schedule repo mocking 필요
         */
        // when
        try {
            String result = planPalService.chat(userId,chatRoomId,prompt);
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
