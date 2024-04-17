package sangwon.wead.controller.model;

import lombok.Builder;
import lombok.Value;

import java.util.Map;

@Builder
@Value
public class PageBarModel {
    String url;
    Map<String,String> queryMap;
    int current;
    boolean prev;
    boolean next;
    int start;
    int end;
}
