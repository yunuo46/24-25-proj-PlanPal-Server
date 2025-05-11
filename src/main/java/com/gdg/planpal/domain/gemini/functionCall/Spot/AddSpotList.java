package com.gdg.planpal.domain.gemini.functionCall.Spot;

import com.example.gemini_ai.gemini.Spot.Spot;
import com.google.cloud.vertexai.api.FunctionDeclaration;
import com.google.cloud.vertexai.api.Schema;
import com.google.cloud.vertexai.api.Type;
import com.google.protobuf.Struct;
import com.google.protobuf.Value;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class AddSpotList {

    public void addSpotList(List<Spot> spotList){
        System.out.println("add spot list function");
        for(Spot spot:spotList){
            System.out.println("spot name : "+spot.spotName+"; spot address :"+spot.spotAddress);
        }
    }

    public FunctionDeclaration getFunctionDeclaration(){
        return FunctionDeclaration.newBuilder()
                .setName("addSpotList")
                .setDescription("여행 장소를 리스트에 저장합니다.")
                .setParameters(
                        Schema.newBuilder()
                                .setType(Type.OBJECT)
                                .putProperties("spotList",
                                        Schema.newBuilder()
                                                .setType(Type.ARRAY)
                                                .setItems(
                                                        Schema.newBuilder()
                                                                .setType(Type.OBJECT)
                                                                .putProperties("spotName", Schema.newBuilder()
                                                                        .setType(Type.STRING)
                                                                        .setDescription("spot name in english").build())
                                                                .putProperties("spotAddress", Schema.newBuilder()
                                                                        .setType(Type.STRING)
                                                                        .setDescription("spot address in english").build())
                                                                .addRequired("spotName")
                                                                .addRequired("spotAddress")
                                                                .build()
                                                )
                                                .build()
                                )
                                .addRequired("spotList")
                                .build()
                )
                .build();

    }
    public List<Spot> structToSpotList(Struct struct){
        List<Spot> spotList = new ArrayList<>();
        Value spotListValue = struct.getFieldsOrDefault("spotList", null);
        if (spotListValue == null || !spotListValue.hasListValue()) {
            return spotList;  // 비어있거나 잘못된 경우 빈 리스트 반환
        }

        for (Value spotValue : spotListValue.getListValue().getValuesList()) {
            if (!spotValue.hasStructValue()) continue;

            Struct spotStruct = spotValue.getStructValue();
            String spotName = spotStruct.getFieldsOrDefault("spotName", Value.newBuilder().setStringValue("").build()).getStringValue();
            String spotAddress = spotStruct.getFieldsOrDefault("spotAddress", Value.newBuilder().setStringValue("").build()).getStringValue();

            Spot spot = new Spot();
            spot.setSpotName(spotName);
            spot.setSpotAddress(spotAddress);
            spotList.add(spot);
        }
        return spotList;
    }


    }

