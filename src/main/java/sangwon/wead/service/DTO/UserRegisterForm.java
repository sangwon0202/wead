package sangwon.wead.service.DTO;

import lombok.Builder;
import lombok.Data;
import sangwon.wead.repository.entity.User;

@Data
@Builder
public class UserRegisterForm {
    private String userId;
    private String password;
    private String nickname;

}
