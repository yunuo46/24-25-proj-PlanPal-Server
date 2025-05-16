package com.gdg.planpal.domain.gemini.functionCall.schedule;

import com.gdg.planpal.domain.map.application.MapService;
import com.gdg.planpal.domain.map.dao.MapPinRepository;
import com.gdg.planpal.domain.map.domain.IconType;
import com.gdg.planpal.domain.map.domain.pin.MapPin;
import com.gdg.planpal.domain.map.dto.request.MapPinRequest;
import com.gdg.planpal.domain.map.dto.request.ScheduleRequest;
import com.gdg.planpal.domain.schedule.application.ScheduleService;
import com.google.cloud.vertexai.api.FunctionDeclaration;
import com.google.cloud.vertexai.api.Schema;
import com.google.cloud.vertexai.api.Type;
import com.google.protobuf.Struct;
import com.google.protobuf.Value;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class AddSchedule {
    private final MapService mapService;
    private final MapPinRepository mapPinRepository;
    public void addSchedule(Long chatroomId,List<Schedule> scheduleList,String userId){
        System.out.println("add Schedule function called");
        System.out.println("--------scheduleList---------");
        for(Schedule schedule:scheduleList){
            System.out.println("["+schedule.spotName+"]"+schedule.startDate+":"+schedule.startTime+"~"+schedule.endDate+":"+schedule.endTime);
        }
        System.out.println("--------scheduleList end---------");

        scheduleList.stream()
                .map(schedule->{
                    MapPin mapPin = mapPinRepository.findById(schedule.getSpotId()).orElse(null);
                    if(mapPin!=null){
                        System.out.println("schedule add error : pin id does not exist");
                    }
                            return new MapPinRequest(
                                    mapPin.getTitle()
                                    ,mapPin.getAddress()
                                    ,mapPin.getContent()
                                    ,mapPin.getType()
                                    ,mapPin.getRating()
                                    , IconType.STAR
                                    ,mapPin.getPlaceId()
                                    ,mapPin.getCoordinates().getLat()
                                    ,mapPin.getCoordinates().getLng()

                                    ,new ScheduleRequest(schedule.getStartDateTime()
                                    ,schedule.getEndDateTime())
                            )   ;
                }).forEach(mapPinRequest -> mapService.savePin(chatroomId,mapPinRequest,userId));

    }
    public FunctionDeclaration getFunctionDeclaration() {
        return FunctionDeclaration.newBuilder()
                .setName("addSchedule")
                .setDescription("여행 일정을 리스트에 저장합니다.")
                .setParameters(
                        Schema.newBuilder()
                                .setType(Type.OBJECT)
                                .putProperties("scheduleList",
                                        Schema.newBuilder()
                                                .setType(Type.ARRAY)
                                                .setItems(
                                                        Schema.newBuilder()
                                                                .setType(Type.OBJECT)
                                                                .putProperties("spotId", Schema.newBuilder()
                                                                        .setType(Type.NUMBER)
                                                                        .setDescription("여행 장소의 고유 ID(모르면 0)").build())
                                                                .putProperties("startDate", Schema.newBuilder()
                                                                        .setType(Type.STRING)
                                                                        .setFormat("date")
                                                                        .setDescription("일정 시작 날짜 (예: 2025-05-01)").build())
                                                                .putProperties("startTime", Schema.newBuilder()
                                                                        .setType(Type.STRING)
                                                                        .setDescription("일정 시작 시간 (예: 14:00)").build())
                                                                .putProperties("endDate", Schema.newBuilder()
                                                                        .setType(Type.STRING)
                                                                        .setFormat("date")
                                                                        .setDescription("일정 종료 날짜 (예: 2025-05-01)").build())
                                                                .putProperties("endTime", Schema.newBuilder()
                                                                        .setType(Type.STRING)
                                                                        .setDescription("일정 종료 시간 (예: 16:00)").build())
                                                                .putProperties("spotName", Schema.newBuilder()
                                                                        .setType(Type.STRING)
                                                                        .setDescription("장소 이름").build())
                                                                .addRequired("spotId")
                                                                .addRequired("startDate")
                                                                .addRequired("startTime")
                                                                .addRequired("endDate")
                                                                .addRequired("endTime")
                                                                .addRequired("spotName")
                                                                .build()
                                                )
                                                .build()
                                )
                                .addRequired("scheduleList")
                                .build()
                )
                .build();
    }

    public List<Schedule> structToScheduleList(Struct struct) {
        List<Schedule> scheduleList = new ArrayList<>();
        Value listValue = struct.getFieldsOrDefault("scheduleList", null);

        if (listValue == null || !listValue.hasListValue()) {
            return scheduleList;  // 비어있거나 잘못된 경우
        }

        for (Value itemValue : listValue.getListValue().getValuesList()) {
            if (!itemValue.hasStructValue()) continue;

            Struct itemStruct = itemValue.getStructValue();
            Schedule schedule = new Schedule();

            // spotId: Long
            Value spotIdValue = itemStruct.getFieldsOrDefault("spotId", Value.newBuilder().setNumberValue(0).build());
            schedule.setSpotId((long) spotIdValue.getNumberValue());

            // 문자열 필드들
            schedule.setStartDate(itemStruct.getFieldsOrDefault("startDate", Value.newBuilder().setStringValue("").build()).getStringValue());
            schedule.setStartTime(itemStruct.getFieldsOrDefault("startTime", Value.newBuilder().setStringValue("").build()).getStringValue());
            schedule.setEndDate(itemStruct.getFieldsOrDefault("endDate", Value.newBuilder().setStringValue("").build()).getStringValue());
            schedule.setEndTime(itemStruct.getFieldsOrDefault("endTime", Value.newBuilder().setStringValue("").build()).getStringValue());
            schedule.setSpotName(itemStruct.getFieldsOrDefault("spotName", Value.newBuilder().setStringValue("").build()).getStringValue());

            scheduleList.add(schedule);
        }

        return scheduleList;
    }
}
