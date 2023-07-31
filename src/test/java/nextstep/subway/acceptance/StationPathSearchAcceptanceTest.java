package nextstep.subway.acceptance;

import nextstep.subway.domain.service.StationPathSearchRequestType;
import nextstep.utils.AcceptanceTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import static nextstep.subway.acceptance.PathSteps.searchStationPath;
import static nextstep.subway.acceptance.StationLineSectionSteps.createStationLineSection;
import static nextstep.subway.acceptance.StationLineSteps.createStationLine;
import static nextstep.subway.acceptance.StationSteps.createStationsAndGetStationMap;


@DisplayName("지하철 경로 조회 기능")
public class StationPathSearchAcceptanceTest extends AcceptanceTest {

    /**
     * Given 1호선 (종로3가 -3KM- 종로5가 -5KM- 동대문 -5KM- 동묘앞)로 이뤄진 노선을 생성한다
     * Given 4호선 (혜화 -1KM- 동대문 -10KM- 동대문역사문화공원)로 이뤄진 노선을 생성한다
     * Given 부산2호선 (양산 -10KM- 남양산)로 이뤄진 노선을 생성한다
     */
    Map<String, Long> stationIdByName;

    @BeforeEach
    public void setUp2() {
        //given
        stationIdByName = createStationsAndGetStationMap(List.of("혜화", "동대문", "동대문역사문화공원", "종로3가", "종로5가", "동묘앞", "양산", "남양산"));

        var line1 = createStationLine("1호선", "blue", "종로3가", "종로5가", BigDecimal.valueOf(3L), 3000L, stationIdByName);
        createStationLineSection(line1, "종로5가", "동대문", BigDecimal.valueOf(5L), 4000L, stationIdByName);
        createStationLineSection(line1, "동대문", "동묘앞", BigDecimal.valueOf(5L), 5000L, stationIdByName);

        var line2 = createStationLine("4호선", "mint", "혜화", "동대문", BigDecimal.ONE, 2000L, stationIdByName);
        createStationLineSection(line2, "동대문", "동대문역사문화공원", BigDecimal.TEN, 6000L, stationIdByName);

        createStationLine("부산2호선", "red", "양산", "남양산", BigDecimal.TEN, 1000L, stationIdByName);
    }

    /**
     * When 종로3가에서 동대문역사문화공원으로 DISTANCE TYPE으로 경로 조회를 요청한다
     * Then 종로3가에서 동대문역사문화공원으로 경로 역의 목록으로 (종로3가, 종로5가, 동대문, 동대문역사문화공원)를 응답한다
     * Then 전체 경로의 최단거리로 18을 응답한다
     * Then 전체 경로의 최소시간으로 13초를 응답한다
     */
    @DisplayName("정상적인 지하철 경로 조회")
    @Test
    void searchStationPathTest() {
        //when
        var searchResponse = searchStationPath("종로3가", "동대문역사문화공원", StationPathSearchRequestType.DISTANCE, HttpStatus.OK);
        var distance = searchResponse.getObject("distance", BigDecimal.class);
        var duration = searchResponse.getLong("duration");
        var pathStationNames = searchResponse.getList("stations.name", String.class);

        //then
        var expectedDistance = BigDecimal.valueOf(18);
        var expectedDuration = 1000 * 13L;

        Assertions.assertEquals(0, expectedDistance.compareTo(distance));
        Assertions.assertEquals(expectedDuration, duration);
        Assertions.assertArrayEquals(List.of("종로3가", "종로5가", "동대문", "동대문역사문화공원").toArray(), pathStationNames.toArray());
    }

    /**
     * When 종로3가에서 종로3가 경로 조회를 요청한다
     * Then 에러 발생
     */
    @DisplayName("출발역과 도착역이 같은 경우의 지하철 경로 조회 시 에러")
    @Test
    void searchStationPath_Same_SourceStation_And_TargetStation() {
        //when & then
        searchStationPath("종로3가", "종로3가", StationPathSearchRequestType.DISTANCE, HttpStatus.BAD_REQUEST);
    }

    /**
     * When 종로3가에서 양산역으로 경로 조회를 요청한다
     * Then 에러 발생
     */
    @DisplayName("출발역과 도착역이 노선상 연결되지 않은 경우 에러")
    @Test
    void searchStationPath_Not_Linked_SourceStation_And_TargetStation() {
        //when & then
        searchStationPath("종로3가", "양산", StationPathSearchRequestType.DISTANCE, HttpStatus.BAD_REQUEST);
    }
}
