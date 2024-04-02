package sangwon.wead.DTO;

import lombok.Data;
import sangwon.wead.repository.entity.User;

@Data
public class UserInfo {
    private String userId;
    private String nickname;

    public UserInfo(User user) {
        this.userId = user.getId();
        this.nickname = user.getNickname();
    }
}
