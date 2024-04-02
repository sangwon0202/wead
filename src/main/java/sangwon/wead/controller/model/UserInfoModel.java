package sangwon.wead.controller.model;

import lombok.Data;
import sangwon.wead.DTO.UserInfo;

@Data
public class UserInfoModel {
    private String nickname;

    public UserInfoModel(UserInfo userDto) {
        this.nickname = userDto.getNickname();
    }
}
