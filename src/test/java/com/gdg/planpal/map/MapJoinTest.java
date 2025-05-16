package com.gdg.planpal.map;

import com.gdg.planpal.domain.map.dao.MapPinRepository;
import com.gdg.planpal.domain.map.dao.MapRepository;
import com.gdg.planpal.domain.map.domain.Coordinates;
import com.gdg.planpal.domain.map.domain.MapBoard;
import com.gdg.planpal.domain.map.domain.pin.HeartMapPin;
import com.gdg.planpal.domain.map.domain.pin.StarMapPin;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Transactional
class MapJoinTest {

    @Autowired
    private MapRepository mapRepository;

    @Autowired
    private MapPinRepository mapPinRepository;

    @BeforeEach
    void setup() {
        MapBoard map = mapRepository.save(MapBoard.builder()
                .centerCoordinates(new Coordinates(0.0, 0.0))
                .build());

        mapPinRepository.saveAll(List.of(
                HeartMapPin.builder()
                        .mapBoard(map)
                        .placeId("p1")
                        .content("heart")
                        .build(),

                StarMapPin.builder()
                        .mapBoard(map)
                        .placeId("p2")
                        .content("star")
                        .build()
        ));
    }

    @Test
    void fetchMapWithPins_noNPlus1() {
        Long chatRoomId = 1L;
        Optional<MapBoard> mapOpt = mapRepository.findWithPinsByChatRoomId(chatRoomId);

        MapBoard map = mapRepository.findAll().getFirst();
        assertThat(map.getPins()).hasSize(2);
    }
}
