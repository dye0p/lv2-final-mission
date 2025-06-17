package finalmission.woowabowling.lane;

import finalmission.woowabowling.lane.domain.Lane;

public record LaneRegisterResponse(
        long id,
        int number,
        String patter
) {
    public static LaneRegisterResponse of(final Lane lane) {
        return new LaneRegisterResponse(
                lane.getId(),
                lane.getNumber(),
                lane.getPatternName()
        );
    }

}
