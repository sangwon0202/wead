package sangwon.wead.API.naver;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@Component
public class NaverAPIBookClient {

    @Value("${naverAPI.client.id}")
    private String clientId;
    @Value("${naverAPI.client.password}")
    private String clientSecret;

    public NaverAPIBookResponse searchBook(String query,
                                           int display,
                                           int start,
                                           String sort) {

        RestClient restClient = RestClient.create();
        String uri = ServletUriComponentsBuilder.newInstance()
                .scheme("https")
                .host("openapi.naver.com")
                .path("/v1/search/book.json")
                .queryParam("query", query)
                .queryParam("display", display)
                .queryParam("start", start)
                .queryParam("sort", sort)
                .build().toUriString();

        return restClient.get()
                .uri(uri)
                .headers((httpHeaders -> {
                    httpHeaders.add("X-Naver-Client-Id", clientId);
                    httpHeaders.add("X-Naver-Client-Secret", clientSecret);
                }))
                .retrieve()
                .body(NaverAPIBookResponse.class);
    }
}