package com.gdg.planpal.calendar;

import com.gdg.planpal.domain.schedule.dao.StarMapPinScheduleRepository;
import com.gdg.planpal.domain.map.dao.MapPinRepository;
import com.gdg.planpal.domain.map.dao.MapRepository;
import com.gdg.planpal.domain.map.domain.Coordinates;
import com.gdg.planpal.domain.map.domain.MapBoard;
import com.gdg.planpal.domain.map.domain.pin.StarMapPin;
import com.gdg.planpal.domain.schedule.domain.StarMapPinSchedule;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.hibernate.Session;
import org.hibernate.stat.Statistics;
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
class ScheduleJoinTest {

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private MapRepository mapRepository;

    @Autowired
    private MapPinRepository mapPinRepository;

    @Autowired
    private StarMapPinScheduleRepository scheduleRepository;

    @BeforeEach
    void setup() {
        MapBoard map = mapRepository.save(MapBoard.builder()
                .centerCoordinates(new Coordinates(2.0, 2.0))
                .build());

        StarMapPin pin = StarMapPin.builder()
                .mapBoard(map)
                .placeId("test_pid")
                .content("schedule test")
                .build();

        StarMapPinSchedule schedule = StarMapPinSchedule.builder()
                .mapPin(pin)
                .startTime(LocalDateTime.now())
                .endTime(LocalDateTime.now().plusHours(1))
                .build();

        pin.getSchedules().add(schedule);
        mapPinRepository.save(pin);
    }

    @Test
    void fetchSchedulesWithMapPin_noNPlus1() {
        //given
        Long mapId = mapRepository.findAll().getLast().getId();

        // enable statistics
        Session session = entityManager.unwrap(Session.class);
        Statistics statistics = session.getSessionFactory().getStatistics();
        statistics.setStatisticsEnabled(true);
        statistics.clear();

        // when
        List<StarMapPinSchedule> schedules = scheduleRepository.findAllByMapId(mapId);

        // then
        assertThat(schedules).hasSize(1);
        assertThat(schedules.getLast().getMapPin()).isNotNull();
        assertThat(schedules.getLast().getMapPin().getPlaceId()).isEqualTo("test_pid");

        assertThat(statistics.getPrepareStatementCount())
                .as("Only 1 query should be prepared if JOIN FETCH works correctly")
                .isEqualTo(1);
    }
}

