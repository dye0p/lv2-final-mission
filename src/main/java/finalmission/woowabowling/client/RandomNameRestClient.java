package finalmission.woowabowling.client;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.client.RestClient;

@RequiredArgsConstructor
public class RandomNameRestClient {

    @Value("${client.random-name.secret-Key}")
    private String secretKey;

    private final RestClient restClient;

    public String request() {
        return restClient.get()
                .uri(uriBuilder -> uriBuilder.path("/api/name")
                        .queryParam("nameType", "fullname")
                        .queryParam("quantity", 1)
                        .build())
                .header("X-Api-Key", secretKey)
                .retrieve()
                .toEntity(String.class)
                .getBody();
    }
}
