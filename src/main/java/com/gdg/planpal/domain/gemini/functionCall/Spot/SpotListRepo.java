package com.gdg.planpal.domain.gemini.functionCall.Spot;

import com.gdg.planpal.domain.map.dao.MapPinRepository;
import com.gdg.planpal.domain.map.domain.pin.MapPin;
import com.google.cloud.vertexai.api.FunctionDeclaration;
import com.google.cloud.vertexai.api.Schema;
import com.google.cloud.vertexai.api.Type;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class SpotListRepo {

    private final MapPinRepository mapPinRepository;

    public Map<Long,String> getSpotList(Long mapId){
        System.out.println("getSpotList called");
        List<MapPin> mapPins = mapPinRepository.findByMapBoardId(mapId);
        return mapPins.stream().collect(Collectors.toMap(MapPin::getId,MapPin::getContent));
    }

    public FunctionDeclaration getFunctionDeclaration(){
        return FunctionDeclaration.newBuilder()
                .setName("getSpotList")
                .setDescription("현재 사용자가 저장 중인 장소 가져오기")
                .setParameters(
                        Schema.newBuilder()
                                .setType(Type.OBJECT)
                                .build()
                )
                .build();
    }
}
