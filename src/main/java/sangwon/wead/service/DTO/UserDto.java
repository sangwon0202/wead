package sangwon.wead.service.DTO;

import lombok.Value;
import sangwon.wead.repository.entity.User;

@Value
public class UserDto {
    String userId;
    String nickname;

    public UserDto(User user) {
        this.userId = user.getUserId();
        this.nickname = user.getNickname();
    }

}
