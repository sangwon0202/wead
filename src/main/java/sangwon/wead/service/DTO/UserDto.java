package sangwon.wead.service.DTO;

import lombok.Data;
import sangwon.wead.repository.entity.User;

@Data
public class UserDto {
    private String userId;
    private String nickname;

    public UserDto(User user) {
        this.userId = user.getUserId();
        this.nickname = user.getNickname();
    }
}
