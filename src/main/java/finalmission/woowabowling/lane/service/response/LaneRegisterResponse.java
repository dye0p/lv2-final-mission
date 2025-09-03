package finalmission.woowabowling.lane.service.response;

import finalmission.woowabowling.lane.domain.Lane;

public record LaneRegisterResponse(
        long id,
        int number,
        String patternName
) {
    public static LaneRegisterResponse of(final Lane lane) {
        return new LaneRegisterResponse(
                lane.getId(),
                lane.getNumber(),
                lane.getPatternName()
        );
    }

}
