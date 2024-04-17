package sangwon.wead.controller.model;

import lombok.Builder;
import lombok.Value;

@Builder
@Value
public class CommonModel {
    boolean login;
    String userId;
    String nickname;
}
