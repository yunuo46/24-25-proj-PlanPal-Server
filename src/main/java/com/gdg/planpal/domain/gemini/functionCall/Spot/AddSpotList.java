package com.gdg.planpal.domain.gemini.functionCall.Spot;

import com.gdg.planpal.domain.googleMap.GoogleMapService;
import com.gdg.planpal.domain.googleMap.PlaceInfoDTO;
import com.gdg.planpal.domain.map.application.MapService;
import com.gdg.planpal.domain.map.application.factory.MapPinFactoryRouter;
import com.gdg.planpal.domain.map.dao.MapPinRepository;
import com.gdg.planpal.domain.map.domain.IconType;
import com.gdg.planpal.domain.map.dto.request.MapPinRequest;
import com.google.cloud.vertexai.api.FunctionDeclaration;
import com.google.cloud.vertexai.api.Schema;
import com.google.cloud.vertexai.api.Type;
import com.google.protobuf.Struct;
import com.google.protobuf.Value;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class AddSpotList {

    private final GoogleMapService googleMapService;
    private final MapService mapService;


    public void addSpotList(String userName, Long mapId, List<Spot> spotList){
        System.out.println("add spot list function");
        if(spotList==null){
            System.out.println("spotList is null");
            return;
        }
        if(spotList.isEmpty()){
            System.out.println("spotList is empty");
            return;
        }

        spotList.stream()
                .map(spot -> {
                    try {
                        return googleMapService.findPlace(spot.getSpotName());
                    }catch(Exception e){
                        System.out.println("error in google map: "+ e);
                        new Exception(e);
                    }
                    return null;
                })
                .map(place->spotToRequest(place))
                .peek(mapPinRequest->System.out.println("spot name : "+mapPinRequest.title()))
                .forEach(mapPinRequest -> mapService.savePin(mapId,mapPinRequest,userName));

    }

    private MapPinRequest spotToRequest( PlaceInfoDTO placeInfoDTO){

        return new MapPinRequest(
                placeInfoDTO.getName()
                , placeInfoDTO.getAddress()
                ,"content"
                ,"type"
                , 5.0
                , IconType.HEART
                , placeInfoDTO.getPlaceId()
                ,placeInfoDTO.getLatitude(),
                placeInfoDTO.getLongitude()
                ,null

        );
    }

    private List<MapPinRequest> spotToRequest( List<PlaceInfoDTO> placeInfoDTOs){

        return placeInfoDTOs.stream()
                .map(placeInfoDTO->{
                    return new MapPinRequest(
                            placeInfoDTO.getName()
                            , placeInfoDTO.getAddress()
                            ,"content"
                            ,"type"
                            , 5.0
                            , IconType.HEART
                            , placeInfoDTO.getPlaceId()
                            ,placeInfoDTO.getLatitude()
                            , placeInfoDTO.getLongitude()
                            ,null

                            );
                }).collect(Collectors.toList());


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

