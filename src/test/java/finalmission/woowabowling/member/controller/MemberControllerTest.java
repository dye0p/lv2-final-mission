package finalmission.woowabowling.member.controller;

import static org.assertj.core.api.Assertions.assertThat;

import finalmission.woowabowling.member.controller.request.LoginRequest;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.annotation.DirtiesContext;

@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class MemberControllerTest {

    @LocalServerPort
    public int port;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    @DisplayName("회원가입에 성공하면 상태코드 201 CREATED와 성공한 경로 + 성공한 회원의 식별자를 반환한다.")
    @Test
    void signup() {
        //given
        LoginRequest request = new LoginRequest("test", "1234");

        //when
        ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .contentType(ContentType.JSON)
                .body(request)
                .when()
                .post("/members")
                .then().log().all()
                .statusCode(201)
                .extract();

        String responseLocation = response.header("Location");

        //then
        assertThat(responseLocation).isEqualTo("/members/1");
    }

}
