package finalmission.woowabowling.member;

public record LoginRequest(

        String email,
        String password
) {
    public Member toMember(final String name) {
        return Member.from(name, email, password);
    }
}
