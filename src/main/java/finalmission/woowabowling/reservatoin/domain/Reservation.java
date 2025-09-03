package finalmission.woowabowling.reservatoin.domain;

import finalmission.woowabowling.lane.domain.Lane;
import finalmission.woowabowling.member.domain.Member;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.time.LocalDate;
import java.time.LocalTime;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(name = "member_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    @JoinColumn(name = "lane_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Lane lane;

    @Column(nullable = false)
    private Long memberCount;

    @Column(nullable = false)
    private Long gameCount;

    @Column(nullable = false)
    private LocalDate date;

    @Column(nullable = false)
    private LocalTime time;

    private Reservation(final Member member, final Lane lane, final Long memberCount, final Long gameCount,
                        final LocalDate date, final LocalTime time) {
        this.member = member;
        this.lane = lane;
        this.memberCount = memberCount;
        this.gameCount = gameCount;
        this.date = date;
        this.time = time;
    }

    public static Reservation from(final Member member, final Lane lane, final Long memberCount, final Long gameCount,
                                   final LocalDate date, final LocalTime time) {
        return new Reservation(member, lane, memberCount, gameCount, date, time);
    }

    public void update(
            final Lane lane,
            final long memberCount,
            final long gameCount,
            final LocalDate date,
            final LocalTime time
    ) {
        this.lane = lane;
        this.memberCount = memberCount;
        this.gameCount = gameCount;
        this.date = date;
        this.time = time;
    }

    public String getMemberName() {
        return member.getName();
    }

    public int getLanNumber() {
        return lane.getNumber();
    }
}
