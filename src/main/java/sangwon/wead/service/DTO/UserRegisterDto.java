package sangwon.wead.service.DTO;

import lombok.Builder;
import lombok.Data;
import sangwon.wead.repository.entity.User;

@Data
@Builder
public class UserRegisterDto {
    private String userId;
    private String password;
    private String nickname;

    public User toUser() {
        return User.builder()
                .userId(this.userId)
                .password(this.password)
                .nickname(this.nickname)
                .build();
    }
}
