package sangwon.wead.service.DTO;

import lombok.Data;
import sangwon.wead.repository.entity.User;

@Data
public class UserInfo {
    private String userId;
    private String nickname;
    private String password;

    public UserInfo(User user) {
        this.userId = user.getId();
        this.nickname = user.getNickname();
        this.password = user.getPassword();
    }
}
