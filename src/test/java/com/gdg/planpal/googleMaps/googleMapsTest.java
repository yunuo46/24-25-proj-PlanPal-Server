package com.gdg.planpal.googleMaps;

import com.gdg.planpal.domain.gemini.GeminiRestService;
import com.gdg.planpal.domain.googleMap.GoogleMapService;
import com.gdg.planpal.domain.googleMap.PlaceInfoDTO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;

public class googleMapsTest {
    private GoogleMapService googleMapService;
    @Test
    @DisplayName("google map test")
    void googleMapTest() throws Exception{
        googleMapService = new GoogleMapService();
        Field field = GoogleMapService.class.getDeclaredField("apiKey");
        field.setAccessible(true);
        field.set(googleMapService, System.getenv("GOOGLE_PLACES_API_KEY"));
        googleMapService.init();
        PlaceInfoDTO placeInfoDTO =googleMapService.findPlace("본다이 비치");

        Assertions.assertNotNull(placeInfoDTO);
        System.out.println(placeInfoDTO.toString());

    }
}
