package com.gdg.planpal.map;

import com.gdg.planpal.domain.map.application.MapService;
import com.gdg.planpal.domain.map.dao.MapPinRepository;
import com.gdg.planpal.domain.map.dao.MapRepository;
import com.gdg.planpal.domain.map.domain.Coordinates;
import com.gdg.planpal.domain.map.domain.IconType;
import com.gdg.planpal.domain.map.domain.MapBoard;
import com.gdg.planpal.domain.map.domain.pin.HeartMapPin;
import com.gdg.planpal.domain.map.domain.pin.MapPin;
import com.gdg.planpal.domain.map.domain.pin.StarMapPin;
import com.gdg.planpal.domain.map.dto.request.MapPinRequest;
import com.gdg.planpal.domain.map.dto.request.ScheduleRequest;
import com.gdg.planpal.domain.user.dao.UserRepository;
import com.gdg.planpal.domain.user.domain.User;
import com.gdg.planpal.infra.domain.oauth.OauthProvider;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Transactional
class MapPinFactoryTest {

    @Autowired
    private MapService mapService;

    @Autowired
    private MapRepository mapRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MapPinRepository mapPinRepository;

    private MapBoard mapBoard;
    private User user;

    @BeforeEach
    void setup() {
        mapBoard = MapBoard.builder()
                .centerCoordinates(new Coordinates(0.0, 0.0))
                .build();
        mapRepository.save(mapBoard);

        user = User.builder()
                .name("테스트유저")
                .email("test@example.com")
                .oauthProvider(OauthProvider.GOOGLE)
                .profileImageUrl("https://example.com/profile.png")
                .build();
        userRepository.save(user);
    }

    @Test
    void HeartPin_create_success() {
        // given
        MapPinRequest request = new MapPinRequest(
                "제목",
                "서울시 어딘가",
                "간단한 설명",
                "type",
                4.5,
                IconType.HEART,
                "place-123",
               37.5665, 126.9780,
                null
        );

        // when
        mapService.savePin(mapBoard.getId(), request, user.getId());

        // then
        List<MapPin> pins = mapPinRepository.findAll();
        assertThat(pins.getLast()).isInstanceOf(HeartMapPin.class);
    }

    @Test
    void StarPin_create_success() {
        // given
        ScheduleRequest schedule = new ScheduleRequest(LocalDateTime.now(), LocalDateTime.now().plusHours(1));

        MapPinRequest request = new MapPinRequest(
                "스케줄 제목",
                "서울시 스팟주소",
                "상세한 설명",
                "type",
                3.7,
                IconType.STAR,
                "place-456",
                37.5665, 126.9780,
                schedule
        );

        // when
        mapService.savePin(mapBoard.getId(), request, user.getId());

        // then
        List<MapPin> pins = mapPinRepository.findAll();
        assertThat(pins.getLast()).isInstanceOf(StarMapPin.class);
        StarMapPin starMapPin = (StarMapPin) pins.getLast();
        assertThat(starMapPin.getSchedules()).hasSize(2);
    }

    @Test
    void HeartPin_to_Star_convert_success() {
        // given
        MapPinRequest heartRequest = new MapPinRequest(
                "제목", "서울시 어딘가", "간단한 설명",
                "type", 4.5, IconType.HEART, "place-789",3.5665, 126.9780,null
        );
        mapService.savePin(mapBoard.getId(), heartRequest, user.getId());

        // when - 같은 장소(placeId)에 대해 Star 핀으로 요청
        ScheduleRequest schedule = new ScheduleRequest(LocalDateTime.now(), LocalDateTime.now().plusHours(2));
        MapPinRequest starRequest = new MapPinRequest(
                "스케줄 제목", "서울시 어딘가", "변경된 설명",
                "type", 4.8, IconType.STAR, "place-789", 7.5665, 126.9780,schedule
        );
        mapService.savePin(mapBoard.getId(), starRequest, user.getId());

        // then - Star 핀만 남고, schedule 포함
        List<MapPin> pins = mapPinRepository.findAll();
        assertThat(pins).hasSize(1);
        assertThat(pins.getFirst()).isInstanceOf(StarMapPin.class);
        StarMapPin starPin = (StarMapPin) pins.getFirst();
        assertThat(starPin.getSchedules()).hasSize(1);
    }

    @Test
    void StarPin_addSchedule_success() {
        // given - 처음 Star 핀 생성
        ScheduleRequest schedule1 = new ScheduleRequest(LocalDateTime.now(), LocalDateTime.now().plusHours(1));
        MapPinRequest request1 = new MapPinRequest(
                "제목", "서울시 어딘가", "설명",
                "type", 4.0, IconType.STAR, "place-999", 39.5665, 126.9780,schedule1
        );
        mapService.savePin(mapBoard.getId(), request1, user.getId());

        // when - 같은 placeId에 대해 다른 schedule 추가
        ScheduleRequest schedule2 = new ScheduleRequest(LocalDateTime.now().plusHours(2), LocalDateTime.now().plusHours(3));
        MapPinRequest request2 = new MapPinRequest(
                "제목", "서울시 어딘가", "설명",
                "type", 4.0, IconType.STAR, "place-999", 38.5665, 126.9780,schedule2
        );
        mapService.savePin(mapBoard.getId(), request2, user.getId());

        // then - Star 핀은 1개, schedule은 2개
        List<MapPin> pins = mapPinRepository.findAll();
        assertThat(pins).hasSize(1);
        assertThat(pins.getFirst()).isInstanceOf(StarMapPin.class);
        StarMapPin starPin = (StarMapPin) pins.getFirst();
        assertThat(starPin.getSchedules()).hasSize(2);
    }

}
