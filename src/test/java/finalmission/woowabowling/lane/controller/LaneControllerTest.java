package finalmission.woowabowling.lane.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import finalmission.woowabowling.lane.controller.request.LaneRegisterRequest;
import finalmission.woowabowling.pattern.domain.Pattern;
import finalmission.woowabowling.pattern.domain.PatternRepository;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.annotation.DirtiesContext;

@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class LaneControllerTest {

    @LocalServerPort
    public int port;

    @Autowired
    private PatternRepository patternRepository;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    @DisplayName("레인 등록에 성공하면 상태코드 201 CREATED와 레인 생성 경로 + 레인 정보를 담은 객체가 반환된다.")
    @Test
    void register() {
        //given
        Pattern pattern = patternRepository.findById(1L)
                .orElse(null);

        LaneRegisterRequest request = new LaneRegisterRequest(1, pattern.getId());

        //when
        ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .contentType(ContentType.JSON)
                .body(request)
                .when()
                .post("/lanes")
                .then().log().all()
                .statusCode(201)
                .extract();

        JsonPath result = response.jsonPath();
        String responseLocation = response.header("Location");

        //then
        assertAll(
                () -> assertThat(result.getLong("id")).isEqualTo(1L),
                () -> assertThat(result.getInt("number")).isEqualTo(1),
                () -> assertThat(result.getString("patternName")).isEqualTo("치타Cheetah)")
        );
        assertThat(responseLocation).isEqualTo("/lanes/1");

    }

}
