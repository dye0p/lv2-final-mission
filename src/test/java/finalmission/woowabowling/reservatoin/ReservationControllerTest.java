package finalmission.woowabowling.reservatoin;

import static org.assertj.core.api.Assertions.assertThat;

import finalmission.woowabowling.member.Member;
import finalmission.woowabowling.member.MemberRepository;
import io.restassured.RestAssured;
import io.restassured.common.mapper.TypeRef;
import io.restassured.http.ContentType;
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

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class ReservationControllerTest {

    @LocalServerPort
    public int port;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private ReservationRepository reservationRepository;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    @DisplayName("예약 등록 요청에 성공하면 상태코드 201CREATED와 예약 정보를 담은 객체가 반환된다")
    @Test
    void register() {
        //given
        Member savedMember = memberRepository.save(Member.from("test", "test", "1234"));
        ReservationRequest request = new ReservationRequest(
                savedMember.getId(),
                1L,
                3L,
                1L,
                LocalDate.of(2025, 1, 1),
                LocalTime.of(10, 0)
        );

        //when
        ReservationResponse response = RestAssured
                .given().log().all()
                .contentType(ContentType.JSON)
                .body(request)
                .when()
                .post("/reservations")
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

    @DisplayName("모든 예약 현황을 조회하면 상태코드 200OK와 예약 정보를 담은 리스트가 반환된다.")
    @Test
    void findAll() {
        //given
        Member savedMember = memberRepository.save(Member.from("test", "test", "1234"));

        Reservation reservation = Reservation.from(
                savedMember,
                1L,
                3L,
                1L,
                LocalDate.of(2025, 1, 1),
                LocalTime.of(10, 0)
        );

        Reservation reservation2 = Reservation.from(
                savedMember,
                1L,
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
                .when()
                .get("/reservations")
                .then().log().all()
                .statusCode(200)
                .extract()
                .as(new TypeRef<>() {
                });

        //then
        assertThat(response).hasSize(2);
    }
}
