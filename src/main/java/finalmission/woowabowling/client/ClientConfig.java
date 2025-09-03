package finalmission.woowabowling.client;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestClient;

@RequiredArgsConstructor
@Configuration
public class ClientConfig {

    private static final String BASE_URL = "https://randommer.io/";

    @Bean
    public RandomNameRestClient randomNameRestClient() {
        return new RandomNameRestClient(
                RestClient.builder()
                        .baseUrl(BASE_URL)
                        .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .build());
    }

}
