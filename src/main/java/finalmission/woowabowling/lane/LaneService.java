package finalmission.woowabowling.lane;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class LaneService {

    private final LaneRepository laneRepository;

    @Transactional
    public LaneRegisterResponse register(final Lane lane) {
        final Lane savedLane = laneRepository.save(lane);
        return LaneRegisterResponse.of(savedLane);
    }

}
