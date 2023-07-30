package nextstep.subway.acceptance;

import io.restassured.RestAssured;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public class StationLineSectionSteps {
    private StationLineSectionSteps() {
    }

    public static void createStationLineSection(Long lineId, String upStationName, String downStationName, BigDecimal distance, Map<String, Long> stationIdByName) {
        createStationLineSection(lineId, stationIdByName.get(upStationName), stationIdByName.get(downStationName), distance, HttpStatus.OK);
    }

    public static void createStationLineSection(Long lineId, Long upStationId, Long downStationId, BigDecimal distance) {
        createStationLineSection(lineId, upStationId, downStationId, distance, HttpStatus.OK);
    }

    public static void createStationLineSection(Long lineId, Long upStationId, Long downStationId, BigDecimal distance, HttpStatus expectedStatus) {
        final Map<String, String> stationLineSectionCreateRequest = new HashMap<>();

        stationLineSectionCreateRequest.put("upStationId", String.valueOf(upStationId));
        stationLineSectionCreateRequest.put("downStationId", String.valueOf(downStationId));
        stationLineSectionCreateRequest.put("distance", distance.toString());

        RestAssured.given().log().all()
                .body(stationLineSectionCreateRequest)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/lines/" + lineId + "/sections")
                .then().log().all()
                .statusCode(expectedStatus.value())
                .extract();
    }

    public static void deleteStationLineSection(Long lineId, Long stationId, HttpStatus expectedStatus) {
        RestAssured.given().log().all()
                .param("stationId", stationId)
                .when().delete("/lines/" + lineId + "/sections")
                .then().log().all()
                .statusCode(expectedStatus.value())
                .extract();
    }
}