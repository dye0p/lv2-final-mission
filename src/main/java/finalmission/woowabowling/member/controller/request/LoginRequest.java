package finalmission.woowabowling.member.controller.request;

public record LoginRequest(
        String email,
        String password
) {
}
