package sangwon.wead.service.DTO;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class UserRegisterDto {
    String userId;
    String password;
    String nickname;
}
