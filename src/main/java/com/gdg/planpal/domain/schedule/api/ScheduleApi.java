package com.gdg.planpal.domain.schedule.api;

import com.gdg.planpal.domain.schedule.application.ScheduleService;
import com.gdg.planpal.domain.schedule.domain.StarMapPinSchedule;
import com.gdg.planpal.domain.map.dto.request.ScheduleRequest;
import com.gdg.planpal.domain.schedule.dto.response.ScheduleResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;

import java.util.List;

@RestController
@RequestMapping("/api/schedules")
@RequiredArgsConstructor
@SecurityRequirement(name = "accessToken")
public class ScheduleApi {

    private final ScheduleService scheduleService;

    @GetMapping("/maps/{chatRoomId}")
    @Operation(summary = "스케쥴 목록 조회", description = "맵 ID를 기준으로 해당 맵에 속한 모든 스케쥴을 조회합니다.")
    public List<ScheduleResponse> getSchedules(@PathVariable Long chatRoomId) {
        return scheduleService.getSchedulesByChatRoomId(chatRoomId);
    }

    @PutMapping("/{scheduleId}")
    @Operation(summary = "스케쥴 수정", description = "스케쥴 ID를 기준으로 스케쥴을 수정합니다.")
    public void updateSchedule(@PathVariable Long scheduleId, @RequestBody ScheduleRequest request){
        scheduleService.updateSchedule(scheduleId, request);
    }

    @DeleteMapping("/{scheduleId}")
    @Operation(summary = "스케쥴 삭제", description = "스케쥴 ID를 기준으로 스케쥴을 삭제합니다.")
    public void deleteSchedule(@PathVariable Long scheduleId) {
        scheduleService.deleteSchedule(scheduleId);
    }
}
