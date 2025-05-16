package com.gdg.planpal.domain.schedule.domain;

import com.gdg.planpal.domain.map.domain.pin.StarMapPin;
import com.gdg.planpal.domain.map.dto.request.ScheduleRequest;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class StarMapPinSchedule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "map_pin_id", nullable = false)
    private StarMapPin mapPin;

    @Column(nullable = false)
    private LocalDateTime startTime;

    @Column(nullable = false)
    private LocalDateTime endTime;

    public void update(ScheduleRequest request) {
        this.startTime = request.startTime();
        this.endTime = request.endTime();
    }

    public String toString(){
        return "["+this.mapPin.getId().toString()+":"+this.mapPin.getContent()+"]"+this.startTime.toString()+"~"+this.endTime;
    }

}
