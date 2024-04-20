package sangwon.wead.controller.DTO;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;
import sangwon.wead.service.DTO.UserRegisterDto;


@Data
public class UserRegisterParam {

    @Pattern(regexp = "^(?=.*[A-Za-z])[A-Za-z0-9]{5,15}$",
            message = "영어를 포함하고 5자 이상 15자 이하여야합니다.")
    private String userId;
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{6,10}$",
            message = "영문자와 숫자를 포함하고 5자 이상 15자 이하여야합니다.")
    private String password;
    @Size(min = 2, max = 10,
            message = "2자 이상 10자 이하여야합니다.")
    private String nickname;

    public UserRegisterDto toUserRegisterForm() {
        return UserRegisterDto.builder()
                .userId(userId)
                .password(password)
                .nickname(nickname)
                .build();
    }

}
