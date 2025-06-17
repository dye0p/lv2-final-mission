package finalmission.woowabowling.member;

import static org.assertj.core.api.Assertions.assertThat;

import finalmission.woowabowling.auth.CookieProvider;
import finalmission.woowabowling.auth.JwtTokenProvider;
import finalmission.woowabowling.reservatoin.ReservationRequest;
import finalmission.woowabowling.reservatoin.ReservationResponse;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import jakarta.servlet.http.Cookie;
import java.time.LocalDate;
import java.time.LocalTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.server.LocalServerPort;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class MemberReservationControllerTest {

    @LocalServerPort
    public int port;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private CookieProvider cookieProvider;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    @DisplayName("특정 사용자의 예약 등록에 성공하면 상태코드 201CREATED와 예약 정보를 담은 객체가 반환된다.")
    @Test
    void register() {
        //given
        Member savedMember = memberRepository.save(Member.from("test", "test", "1234"));
        ReservationRequest request = new ReservationRequest(
                1L,
                3L,
                1L,
                LocalDate.of(2025, 1, 1),
                LocalTime.of(10, 0)
        );

        String token = jwtTokenProvider.createToken(savedMember);
        Cookie cookie = cookieProvider.createCookie("token", token);

        //when
        ReservationResponse response = RestAssured
                .given().log().all()
                .contentType(ContentType.JSON)
                .cookie(cookie.getName(), cookie.getValue())
                .body(request)
                .when()
                .post("/reservations-mine")
                .then().log().all()
                .statusCode(201)
                .extract()
                .as(ReservationResponse.class);

        //then
        ReservationResponse compareResponse = new ReservationResponse(
                1L,
                "test",
                1L,
                3L,
                1L,
                LocalDate.of(2025, 1, 1),
                LocalTime.of(10, 0)
        );

        assertThat(response).isEqualTo(compareResponse);
    }

}
