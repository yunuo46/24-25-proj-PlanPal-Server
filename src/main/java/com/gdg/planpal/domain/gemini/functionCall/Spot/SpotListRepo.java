package com.gdg.planpal.domain.gemini.functionCall.Spot;

import com.google.cloud.vertexai.api.FunctionDeclaration;
import com.google.cloud.vertexai.api.Schema;
import com.google.cloud.vertexai.api.Type;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.function.Function;

@Component
public class SpotListRepo {
    public Map<String,String> getSpotList(){
        System.out.println("getSpotList called");
        return Map.of("spot-address",List.of("Royal Botanic Gardens Melbourne, Birdwood Ave, South Yarra VIC 3141", "Australia, National Gallery of Victoria, 180 St Kilda Rd, Melbourne VIC 3006", "Australia, Federation Square, Swanston St & Flinders St, Melbourne VIC 3000, Australia, Queen Victoria Market")
                .toString());
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
