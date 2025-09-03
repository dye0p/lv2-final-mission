package finalmission.woowabowling.reservatoin.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import finalmission.woowabowling.auth.CookieProvider;
import finalmission.woowabowling.auth.JwtTokenProvider;
import finalmission.woowabowling.lane.domain.Lane;
import finalmission.woowabowling.lane.domain.LaneRepository;
import finalmission.woowabowling.member.domain.Member;
import finalmission.woowabowling.member.domain.MemberRepository;
import finalmission.woowabowling.pattern.domain.Pattern;
import finalmission.woowabowling.pattern.domain.PatternRepository;
import finalmission.woowabowling.reservatoin.controller.request.ReservationRegisterRequest;
import finalmission.woowabowling.reservatoin.controller.request.ReservationUpdateRequest;
import finalmission.woowabowling.reservatoin.domain.Reservation;
import finalmission.woowabowling.reservatoin.domain.ReservationRepository;
import finalmission.woowabowling.reservatoin.service.response.ReservationRegisterResponse;
import io.restassured.RestAssured;
import io.restassured.common.mapper.TypeRef;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import jakarta.servlet.http.Cookie;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
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
class MemberReservationControllerTest {

    @LocalServerPort
    public int port;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private LaneRepository laneRepository;

    @Autowired
    private PatternRepository patternRepository;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private CookieProvider cookieProvider;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    @DisplayName("특정 사용자의 예약 등록에 성공하면 상태코드 201CREATED와 예약 생성 경로 + 예약 정보를 담은 객체가 반환된다.")
    @Test
    void register() {
        //given
        Member savedMember = memberRepository.save(Member.from("test", "test", "1234"));

        Pattern pattern = patternRepository.findById(1L)
                .orElse(null);

        Lane savedLane = laneRepository.save(Lane.of(1, pattern));

        ReservationRegisterRequest request = new ReservationRegisterRequest(
                savedLane.getId(),
                3L,
                1L,
                LocalDate.of(2025, 1, 1),
                LocalTime.of(10, 0)
        );

        String token = jwtTokenProvider.createToken(savedMember);
        Cookie cookie = cookieProvider.createCookie("token", token);

        //when
        ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .contentType(ContentType.JSON)
                .cookie(cookie.getName(), cookie.getValue())
                .body(request)
                .when()
                .post("/reservations-mine")
                .then().log().all()
                .statusCode(201)
                .extract();

        JsonPath result = response.jsonPath();
        String responseLocation = response.header("Location");

        //then
        assertAll(
                () -> assertThat(result.getLong("id")).isEqualTo(1L),
                () -> assertThat(result.getInt("laneNumber")).isEqualTo(1),
                () -> assertThat(result.getLong("memberCount")).isEqualTo(3L),
                () -> assertThat(result.getLong("gameCount")).isEqualTo(1L),

                () -> assertThat(result.getString("reservationDate"))
                        .isEqualTo("2025-01-01"),

                () -> assertThat(result.getString("reservationTime"))
                        .isEqualTo("10:00:00")
        );

        assertThat(responseLocation).isEqualTo("/reservations-mine/1");
    }

    @DisplayName("사용자 자신의 예약 정보 조회에 성공하면 상태코드 200 OK와 예약 정보를 담은 리스트가 반환된다.")
    @Test
    void findAll() {
        //given
        Member savedMember = memberRepository.save(Member.from("test", "test", "1234"));
        Pattern pattern = patternRepository.findById(1L)
                .orElse(null);

        Lane savedLane = laneRepository.save(Lane.of(1, pattern));

        String token = jwtTokenProvider.createToken(savedMember);
        Cookie cookie = cookieProvider.createCookie("token", token);

        Reservation reservation = Reservation.from(
                savedMember,
                savedLane,
                3L,
                1L,
                LocalDate.of(2025, 1, 1),
                LocalTime.of(10, 0)
        );

        Reservation reservation2 = Reservation.from(
                savedMember,
                savedLane,
                3L,
                1L,
                LocalDate.of(2025, 1, 2),
                LocalTime.of(10, 0)
        );

        reservationRepository.save(reservation);
        reservationRepository.save(reservation2);

        //when
        List<ReservationRegisterResponse> response = RestAssured
                .given().log().all()
                .contentType(ContentType.JSON)
                .cookie(cookie.getName(), cookie.getValue())
                .when()
                .get("/reservations-mine")
                .then().log().all()
                .statusCode(200)
                .extract()
                .as(new TypeRef<>() {
                });

        //then
        assertThat(response).hasSize(2);
    }

    @DisplayName("사용자는 자신의 예약에 대한 취소가 성공하면 상태코드 200 OK와 취소된 예약의 식별자가 반환된다.")
    @Test
    void cancel() {
        //given
        Member savedMember = memberRepository.save(Member.from("test", "test", "1234"));

        Pattern pattern = patternRepository.findById(1L)
                .orElse(null);

        Lane savedLane = laneRepository.save(Lane.of(1, pattern));

        String token = jwtTokenProvider.createToken(savedMember);
        Cookie cookie = cookieProvider.createCookie("token", token);

        Reservation reservation = Reservation.from(
                savedMember,
                savedLane,
                3L,
                1L,
                LocalDate.of(2025, 1, 1),
                LocalTime.of(10, 0)
        );

        Reservation savedReservation = reservationRepository.save(reservation);

        //when
        Long response = RestAssured
                .given().log().all()
                .contentType(ContentType.JSON)
                .cookie(cookie.getName(), cookie.getValue())
                .when()
                .delete("/reservations-mine/" + savedReservation.getId())
                .then().log().all()
                .statusCode(200)
                .extract()
                .as(Long.class);

        //then
        assertThat(response).isEqualTo(savedMember.getId());
    }

    @DisplayName("사용자는 자신의 예약에 대해 수정에 성공하면 상태코드 200 OK와 수정에 성공한 예약의 정보를 담은 객체가 반환된다.")
    @Test
    void update() {
        //given
        Member savedMember = memberRepository.save(Member.from("test", "test", "1234"));

        Pattern pattern = patternRepository.findById(1L)
                .orElse(null);

        Lane savedLane = laneRepository.save(Lane.of(1, pattern));

        Pattern pattern2 = patternRepository.findById(2L)
                .orElse(null);

        Lane savedLane2 = laneRepository.save(Lane.of(2, pattern2));

        String token = jwtTokenProvider.createToken(savedMember);
        Cookie cookie = cookieProvider.createCookie("token", token);

        Reservation reservation = Reservation.from(
                savedMember,
                savedLane,
                3L,
                1L,
                LocalDate.of(2025, 1, 1),
                LocalTime.of(10, 0)
        );

        Reservation savedReservation = reservationRepository.save(reservation);

        ReservationUpdateRequest request = new ReservationUpdateRequest(
                savedLane2.getId(),
                4L,
                2L,
                LocalDate.of(2025, 1, 2),
                LocalTime.of(10, 30)
        );

        //when
        ReservationRegisterResponse response = RestAssured
                .given().log().all()
                .contentType(ContentType.JSON)
                .cookie(cookie.getName(), cookie.getValue())
                .body(request)
                .when()
                .post("/reservations-mine/" + savedReservation.getId())
                .then().log().all()
                .statusCode(200)
                .extract()
                .as(ReservationRegisterResponse.class);

        //then
        ReservationRegisterResponse compareResponse = new ReservationRegisterResponse(
                1L,
                "test",
                savedLane2.getNumber(),
                4L,
                2L,
                LocalDate.of(2025, 1, 2),
                LocalTime.of(10, 30)
        );
        assertThat(response).isEqualTo(compareResponse);
    }

}
