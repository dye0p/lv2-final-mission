package finalmission.woowabowling.lane.domain;


import finalmission.woowabowling.pattern.domain.Pattern;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class Lane {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Integer number;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pattern_id", nullable = false)
    private Pattern pattern;

    private Lane(final Integer number, final Pattern pattern) {
        this.number = number;
        this.pattern = pattern;
    }

    public static Lane of(final int number, final Pattern pattern) {
        return new Lane(number, pattern);
    }

    public String getPatternName() {
        return pattern.getName();
    }
}
