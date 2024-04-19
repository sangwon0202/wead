package sangwon.wead.service.DTO;

import lombok.Builder;
import lombok.Value;

import java.util.Map;

@Value
@Builder
public class PageBarDto {
    String url;
    Map<String, Object> queryMap;
    int current;
    boolean prev;
    boolean next;
    int start;
    int end;

}
