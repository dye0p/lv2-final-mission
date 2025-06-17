package finalmission.woowabowling.lane.controller;

import static org.assertj.core.api.Assertions.assertThat;

import finalmission.woowabowling.lane.controller.request.LaneRegisterRequest;
import finalmission.woowabowling.lane.service.response.LaneRegisterResponse;
import finalmission.woowabowling.pattern.domain.Pattern;
import finalmission.woowabowling.pattern.domain.PatternRepository;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
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

    @DisplayName("레인 등록에 성공하면 상태코드 201 CREATED와 레인 정보를 담은 객체가 반환된다.")
    @Test
    void register() {
        //given
        Pattern pattern = patternRepository.findById(1L)
                .orElse(null);

        LaneRegisterRequest request = new LaneRegisterRequest(1, pattern.getId());

        //when
        LaneRegisterResponse response = RestAssured
                .given().log().all()
                .contentType(ContentType.JSON)
                .body(request)
                .when()
                .post("/lanes")
                .then().log().all()
                .statusCode(201)
                .extract()
                .as(LaneRegisterResponse.class);

        //then
        LaneRegisterResponse compareResponse = new LaneRegisterResponse(1L, 1, pattern.getName());
        assertThat(response).isEqualTo(compareResponse);
    }

}
