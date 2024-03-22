package sangwon.wead.API.book;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import sangwon.wead.exception.APICallFailException;

@Component
public class NaverAPIBookClient {

    @Value("${naver.api.book.client.id}")
    private String clientId;
    @Value("${naver.api.book.client.secret}")
    private String clientSecret;

    public BookResponse search(String query,
                               int display,
                               int start,
                               String sort) throws APICallFailException {

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

        try {
            return restClient.get()
                    .uri(uri)
                    .headers((httpHeaders -> {
                        httpHeaders.add("X-Naver-Client-Id", clientId);
                        httpHeaders.add("X-Naver-Client-Secret", clientSecret);
                    }))
                    .retrieve()
                    .body(BookResponse.class);
        } catch (Exception e) {
            throw new APICallFailException();
        }

    }
}
