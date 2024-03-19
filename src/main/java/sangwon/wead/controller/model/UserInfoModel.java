package sangwon.wead.controller.model;

import lombok.Data;
import sangwon.wead.service.DTO.UserDto;

@Data
public class UserInfoModel {
    private String nickname;

    public UserInfoModel(UserDto userDto) {
        this.nickname = userDto.getNickname();
    }
}
