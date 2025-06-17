package finalmission.woowabowling.lane;

import finalmission.woowabowling.pattern.Pattern;
import finalmission.woowabowling.pattern.PatternRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class LaneService {

    private final LaneRepository laneRepository;
    private final PatternRepository patternRepository;

    @Transactional
    public LaneRegisterResponse register(final LaneRegisterRequest request) {
        final Pattern pattern = findPattern(request.patternId());
        final Lane lane = Lane.of(request.number(), pattern);
        final Lane savedLane = laneRepository.save(lane);
        return LaneRegisterResponse.of(savedLane);
    }

    private Pattern findPattern(final Long id) {
        return patternRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 패턴 입니다."));
    }

}
