package com.gdg.planpal.domain.googleMap;

import com.google.maps.GeoApiContext;
import com.google.maps.PlaceDetailsRequest;
import com.google.maps.TextSearchRequest;
import com.google.maps.errors.ApiException;
import com.google.maps.model.PlaceDetails;
import com.google.maps.model.PlacesSearchResponse;
import com.google.maps.model.PlacesSearchResult;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class GoogleMapService {

    @Value("${spring.google.places.api.key}")
    private String apiKey;

    private GeoApiContext context;

    @PostConstruct
    public void init() {
        // 애플리케이션 시작 시 GeoApiContext 초기화
        System.out.println("google map api key : "+apiKey);
        context = new GeoApiContext.Builder()
                .apiKey(apiKey)
                // 다른 설정 (ex: 타임아웃) 추가 가능
                .build();
    }

    @PreDestroy
    public void destroy() {
        // 애플리케이션 종료 시 context 정리
        if (context != null) {
            context.shutdown();
        }
    }


    /**
     * 장소 이름 또는 주소로 장소를 검색하고 상세 정보를 반환합니다.
     * Place Search와 Place Details를 순차적으로 호출합니다.
     *
     * @param query 장소 이름 또는 주소
     * @return 장소 상세 정보 (PlaceDetails), 찾지 못한 경우 null
     * @throws ApiException Google API 호출 시 오류 발생
     * @throws IOException 네트워크 또는 데이터 처리 오류
     * @throws InterruptedException 스레드 중단 오류
     */
    public PlaceInfoDTO findPlace(String query) {
        // 1. Text Search 실행
        PlacesSearchResponse searchResponse;
        try {
            searchResponse = new TextSearchRequest(context)
                    .query(query)
                    // 검색 결과의 지역 편향성을 위해 Location 및 Radius 지정 가능
                    // .location(new com.google.maps.model.LatLng(YOUR_LAT, YOUR_LNG))
                    // .radius(5000) // 미터 단위
                    .await(); // API 호출 대기
        }catch (Exception e){
           System.out.println(e);
           return null;
        }
        if (searchResponse.results == null || searchResponse.results.length == 0) {
            // 검색 결과 없음
            return null;
        }

        // 2. 첫 번째 검색 결과의 Place ID를 사용하여 Place Details 실행


        return mapPlaceResponseToDTO(searchResponse.results[0]);
    }

    private PlaceInfoDTO mapPlaceResponseToDTO(PlacesSearchResult result){
        if (result == null) {
            return null;
        }
        PlaceInfoDTO dto = new PlaceInfoDTO();
        dto.setPlaceId(result.placeId);
        dto.setName(result.name);
        dto.setAddress(result.formattedAddress);
        dto.setLatitude(result.geometry.location.lat);
        dto.setLongitude(result.geometry.location.lng);
        if (result.geometry != null && result.geometry.location != null) {
            dto.setLatitude(result.geometry.location.lat);
            dto.setLongitude(result.geometry.location.lng);
        }
        return dto;
    }


    public PlaceInfoDTO findPlaceDetails(String placeId) throws ApiException, IOException, InterruptedException{
        PlaceDetails details = new PlaceDetailsRequest(context)
                .placeId(placeId)
                .fields(PlaceDetailsRequest.FieldMask.PLACE_ID, PlaceDetailsRequest.FieldMask.NAME,
                        PlaceDetailsRequest.FieldMask.FORMATTED_ADDRESS,
                        PlaceDetailsRequest.FieldMask.GEOMETRY_LOCATION,
                        PlaceDetailsRequest.FieldMask.PLUS_CODE, // 추가 정보 예시
                        PlaceDetailsRequest.FieldMask.TYPES // 추가 정보 예시
                        // 필요한 다른 필드 추가...
                )
                .await(); // API 호출 대기

        // 4. 필요한 정보만 DTO에 담아 반환
        return mapToPlaceInfoDTO(details);
    }

    // Google PlaceDetails 객체에서 필요한 정보만 담을 DTO 클래스
    private PlaceInfoDTO mapToPlaceInfoDTO(PlaceDetails details) {
        if (details == null) {
            return null;
        }
        PlaceInfoDTO dto = new PlaceInfoDTO();
        dto.setPlaceId(details.placeId);
        dto.setName(details.name);
        dto.setAddress(details.formattedAddress);
        if (details.geometry != null && details.geometry.location != null) {
            dto.setLatitude(details.geometry.location.lat);
            dto.setLongitude(details.geometry.location.lng);
        }
        // 필요하다면 details 객체에서 다른 정보도 추출하여 DTO에 추가

        return dto;
    }
}
