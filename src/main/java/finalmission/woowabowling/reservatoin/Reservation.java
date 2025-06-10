package finalmission.woowabowling.reservatoin;

import finalmission.woowabowling.member.Member;
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Column(nullable = false)
    private Long laneId;

    @Column(nullable = false)
    private Long memberCount;

    @Column(nullable = false)
    private Long gameCount;

    @Column(nullable = false)
    private LocalDate date;

    @Column(nullable = false)
    private LocalTime time;

    private Reservation(final Member member, final Long laneId, final Long memberCount, final Long gameCount,
                        final LocalDate date, final LocalTime time) {
        this.member = member;
        this.laneId = laneId;
        this.memberCount = memberCount;
        this.gameCount = gameCount;
        this.date = date;
        this.time = time;
    }

    public static Reservation from(final Member member, final Long laneId, final Long memberCount, final Long gameCount,
                                   final LocalDate date, final LocalTime time) {
        return new Reservation(member, laneId, memberCount, gameCount, date, time);
    }

    public String getMemberName() {
        return member.getName();
    }
}
