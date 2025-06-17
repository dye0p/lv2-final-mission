package finalmission.woowabowling.lane;

public record LaneRegisterRequest(
        int number,
        String pattern
) {

    public Lane toEntity() {
        return Lane.of(this.number, this.pattern);
    }
}
