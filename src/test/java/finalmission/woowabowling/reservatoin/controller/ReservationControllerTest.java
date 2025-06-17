package finalmission.woowabowling.reservatoin.controller;

import static org.assertj.core.api.Assertions.assertThat;

import finalmission.woowabowling.lane.domain.Lane;
import finalmission.woowabowling.lane.domain.LaneRepository;
import finalmission.woowabowling.member.domain.Member;
import finalmission.woowabowling.member.domain.MemberRepository;
import finalmission.woowabowling.pattern.domain.Pattern;
import finalmission.woowabowling.pattern.domain.PatternRepository;
import finalmission.woowabowling.reservatoin.domain.Reservation;
import finalmission.woowabowling.reservatoin.domain.ReservationRepository;
import finalmission.woowabowling.reservatoin.service.response.ReservationRegisterResponse;
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
import org.springframework.test.annotation.DirtiesContext;

@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class ReservationControllerTest {

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

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    @DisplayName("모든 예약 현황을 조회하면 상태코드 200OK와 예약 정보를 담은 리스트가 반환된다.")
    @Test
    void findAll() {
        //given
        Member savedMember = memberRepository.save(Member.from("test", "test", "1234"));

        Pattern pattern = patternRepository.findById(1L)
                .orElse(null);

        Lane savedLane = laneRepository.save(Lane.of(1, pattern));

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
