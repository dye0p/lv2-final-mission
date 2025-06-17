package finalmission.woowabowling.member;

import static org.assertj.core.api.Assertions.assertThat;

import finalmission.woowabowling.auth.CookieProvider;
import finalmission.woowabowling.auth.JwtTokenProvider;
import finalmission.woowabowling.lane.Lane;
import finalmission.woowabowling.lane.LaneRepository;
import finalmission.woowabowling.reservatoin.Reservation;
import finalmission.woowabowling.reservatoin.ReservationRepository;
import finalmission.woowabowling.reservatoin.ReservationRequest;
import finalmission.woowabowling.reservatoin.ReservationResponse;
import finalmission.woowabowling.reservatoin.UpdateReservationRequest;
import io.restassured.RestAssured;
import io.restassured.common.mapper.TypeRef;
import io.restassured.http.ContentType;
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
        Lane savedLane = laneRepository.save(Lane.of(1, "testPattern"));

        ReservationRequest request = new ReservationRequest(
                savedLane.getId(),
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
                savedLane.getNumber(),
                3L,
                1L,
                LocalDate.of(2025, 1, 1),
                LocalTime.of(10, 0)
        );
        assertThat(response).isEqualTo(compareResponse);
    }

    @DisplayName("사용자 자신의 예약 정보 조회에 성공하면 상태코드 200 OK와 예약 정보를 담은 리스트가 반환된다.")
    @Test
    void findAll() {
        //given
        Member savedMember = memberRepository.save(Member.from("test", "test", "1234"));
        Lane savedLane = laneRepository.save(Lane.of(1, "testPattern"));

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
        List<ReservationResponse> response = RestAssured
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
        Lane savedLane = laneRepository.save(Lane.of(1, "testPattern"));

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
        Lane savedLane = laneRepository.save(Lane.of(1, "testPattern"));
        Lane savedLane2 = laneRepository.save(Lane.of(2, "testPattern2"));

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

        UpdateReservationRequest request = new UpdateReservationRequest(
                savedLane2.getId(),
                4L,
                2L,
                LocalDate.of(2025, 1, 2),
                LocalTime.of(10, 30)
        );

        //when
        ReservationResponse response = RestAssured
                .given().log().all()
                .contentType(ContentType.JSON)
                .cookie(cookie.getName(), cookie.getValue())
                .body(request)
                .when()
                .post("/reservations-mine/" + savedReservation.getId())
                .then().log().all()
                .statusCode(200)
                .extract()
                .as(ReservationResponse.class);

        //then
        ReservationResponse compareResponse = new ReservationResponse(
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
