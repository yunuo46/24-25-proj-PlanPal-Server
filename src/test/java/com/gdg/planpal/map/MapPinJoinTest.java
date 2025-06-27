package com.gdg.planpal.map;

import com.gdg.planpal.domain.map.dao.MapPinRepository;
import com.gdg.planpal.domain.map.dao.MapRepository;
import com.gdg.planpal.domain.map.domain.Coordinates;
import com.gdg.planpal.domain.map.domain.MapBoard;
import com.gdg.planpal.domain.map.domain.pin.StarMapPin;
import com.gdg.planpal.domain.schedule.domain.StarMapPinSchedule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Transactional
class MapPinJoinTest {

    @Autowired
    private MapRepository mapRepository;

    @Autowired
    private MapPinRepository mapPinRepository;

    @BeforeEach
    void setUp() {
        MapBoard map = mapRepository.save(MapBoard.builder()
                .centerCoordinates(new Coordinates(1.0, 1.0))
                .build());

        StarMapPin pin = StarMapPin.builder()
                .mapBoard(map)
                .placeId("place")
                .content("content")
                .build();

        pin.getSchedules().add(
                StarMapPinSchedule.builder()
                        .mapPin(pin)
                        .startTime(LocalDateTime.now())
                        .endTime(LocalDateTime.now().plusHours(1))
                        .build()
        );

        mapPinRepository.save(pin);
    }

    @Test
    void fetchStarMapPinsWithSchedules_noNPlus1() {
        Long mapId = mapRepository.findAll().getFirst().getId();
        List<StarMapPin> pins = mapPinRepository.findAllWithSchedulesByMapBoardId(mapId);

        assertThat(pins).hasSize(1);
        assertThat(pins.getFirst().getSchedules()).hasSize(1);
    }
}
