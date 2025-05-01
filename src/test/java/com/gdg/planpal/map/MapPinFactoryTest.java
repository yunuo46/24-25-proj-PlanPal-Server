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
    private MapPinRepository mapPinRepository;

    private MapBoard mapBoard;

    @BeforeEach
    void setup() {
        mapBoard = MapBoard.builder()
                .mapId("test-map")
                .centorCoordinates(new Coordinates(0.0, 0.0))
                .zoom(10)
                .build();
        mapRepository.save(mapBoard);
    }

    @Test
    void HeartPin_create_success() {
        // given
        MapPinRequest request = new MapPinRequest(
                37.123, 127.456, "내용", IconType.HEART, "place-123"
        );

        // when
        mapService.savePin(mapBoard.getId(), request);

        // then
        List<MapPin> pins = mapPinRepository.findAll();
        assertThat(pins.getLast()).isInstanceOf(HeartMapPin.class);
    }

    @Test
    void StarPin_create_success() {
        // given
        List<ScheduleRequest> schedules = List.of(
                new ScheduleRequest(1L, LocalDateTime.now(), LocalDateTime.now().plusHours(1))
        );

        MapPinRequest request = new MapPinRequest(
                37.123, 127.456, "스케줄 내용", IconType.STAR, "place-456"
        );

        // when
        mapService.savePin(mapBoard.getId(), request);

        // then
        List<MapPin> pins = mapPinRepository.findAll();
        assertThat(pins.getLast()).isInstanceOf(StarMapPin.class);
    }
}
