package sangwon.wead.service.DTO;

import lombok.Value;
import sangwon.wead.repository.entity.User;

import java.time.LocalDate;

@Value
public class UserDetailDto {

    String userId;
    String nickname;
    LocalDate registerDate;
    int postCount;
    int commentCount;

    public UserDetailDto(User user, int postCount, int commentCount) {
        this.userId = user.getUserId();
        this.nickname = user.getNickname();
        this.registerDate = user.getRegisterDate();
        this.postCount = postCount;
        this.commentCount = commentCount;
    }

}
