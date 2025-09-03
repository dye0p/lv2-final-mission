package finalmission.woowabowling.lane.service;

import finalmission.woowabowling.lane.domain.Lane;
import finalmission.woowabowling.lane.domain.LaneRepository;
import finalmission.woowabowling.lane.service.response.LaneRegisterResponse;
import finalmission.woowabowling.pattern.domain.Pattern;
import finalmission.woowabowling.pattern.domain.PatternRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class LaneService {

    private final LaneRepository laneRepository;
    private final PatternRepository patternRepository;

    @Transactional
    public LaneRegisterResponse register(final int number, final Long patternId) {
        final Pattern pattern = findPattern(patternId);
        final Lane lane = Lane.of(number, pattern);
        final Lane savedLane = laneRepository.save(lane);
        return LaneRegisterResponse.of(savedLane);
    }

    private Pattern findPattern(final Long id) {
        return patternRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 패턴 입니다."));
    }

}
