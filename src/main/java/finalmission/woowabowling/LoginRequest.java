package finalmission.woowabowling;

public record LoginRequest(
        String name,
        String email,
        String password
) {
    public Member toMember() {
        return Member.from(name, email, password);
    }
}
