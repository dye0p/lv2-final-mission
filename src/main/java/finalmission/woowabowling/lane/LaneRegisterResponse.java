package finalmission.woowabowling.lane;

public record LaneRegisterResponse(
        long id,
        int number,
        String patter
) {
    public static LaneRegisterResponse of(final Lane savedLane) {
        return new LaneRegisterResponse(
                savedLane.getId(),
                savedLane.getNumber(),
                savedLane.getPattern()
        );
    }

}
