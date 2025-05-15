package com.gdg.planpal.geminiAi.functionCall.Spot;

import com.gdg.planpal.domain.gemini.functionCall.Spot.SpotListRepo;
import com.gdg.planpal.domain.map.dao.MapPinRepository;
import com.gdg.planpal.domain.map.domain.MapBoard;
import com.gdg.planpal.domain.map.domain.pin.MapPin;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class spotRepoTest {

    @Mock
    private MapPinRepository mapPinRepository;

    @InjectMocks
    private SpotListRepo spotListRepo;


    private Long mapBoardId = 1L;

    @BeforeEach
    void setUp() {
        mapPinRepository = mock(MapPinRepository.class);
        MapPin pin1 = mock(MapPin.class);
        when(pin1.getId()).thenReturn(101L);
        when(pin1.getPlaceName()).thenReturn("Royal Botanic Gardens Melbourne");

        MapPin pin2 = mock(MapPin.class);
        when(pin2.getId()).thenReturn(102L);
        when(pin2.getPlaceName()).thenReturn("National Gallery of Victoria");

        List<MapPin> mockPins = Arrays.asList(pin1, pin2);
        when(mapPinRepository.findByMapBoardId(mapBoardId)).thenReturn(mockPins);

        spotListRepo = new SpotListRepo(mapPinRepository);
    }

    @Test
    @DisplayName("spotRepo mapRepo 연결성 테스트")
    void getSpotList_shouldReturnCorrectMap() {
        // given


        // when
        Map<Long, String> result = spotListRepo.getSpotList(mapBoardId);


        // then
        assertEquals(2, result.size());
        assertEquals("Royal Botanic Gardens Melbourne", result.get(101L));
        assertEquals("National Gallery of Victoria", result.get(102L));
    }

    @Test
    @DisplayName("spotRepo function declare 테스트")
    void getFunctionDeclaration_shouldReturnCorrectNameAndDescription() {
        // when
        var declaration = spotListRepo.getFunctionDeclaration();

        // then
        assertEquals("getSpotList", declaration.getName());
        assertEquals("현재 사용자가 저장 중인 장소 가져오기", declaration.getDescription());

    }

}
