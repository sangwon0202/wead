package sangwon.wead.controller.model;

import lombok.Builder;
import lombok.Value;

import java.util.Map;

@Builder
@Value
public class SearchBarModel {
    String url;
    Map<String, String> optionMap;
}
