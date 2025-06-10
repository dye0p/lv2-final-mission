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

    @Bean
    public RandomNameRestClient randomNameRestClient() {
        return new RandomNameRestClient(
                RestClient.builder()
                        .baseUrl("https://randommer.io/")
                        .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .build());
    }

}
