package nextstep.subway.documentation;

import io.restassured.RestAssured;
import nextstep.subway.applicaion.PathService;
import nextstep.subway.applicaion.dto.PathResponse;
import nextstep.subway.applicaion.dto.StationResponse;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;


import static nextstep.subway.acceptance.PathSteps.지하철_경로_조회;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.document;

public class PathDocumentation extends Documentation {

    @MockBean
    private PathService pathService;

    private Long sourceId = 1L;
    private Long targetId = 1L;

    @Test
    void path() {
        PathResponse pathResponse = new PathResponse(
                Lists.newArrayList(
                        new StationResponse(sourceId, "강남역"),
                        new StationResponse(targetId, "역삼역")
                ), 10
        );

        when(pathService.findPath(anyLong(), anyLong())).thenReturn(pathResponse);

        var doc = document("path",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                responseFields(
                        fieldWithPath(".distance").description("지하철 경로 전체 거리"),
                        fieldWithPath(".stations[]").description("지하철 경로 역 목록"),
                        fieldWithPath(".stations[].id").description("지하철 역 id"),
                        fieldWithPath(".stations[].name").description("지하철 역 이름")
                ));

        지하철_경로_조회(spec, doc, sourceId, targetId);
    }
}
