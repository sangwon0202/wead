package sangwon.wead.controller.DTO;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import sangwon.wead.service.DTO.UserRegisterForm;

@Data
public class UserRegisterParam {

    @NotBlank(message = "아이디를 입력해주세요.")
    private String userId;
    @NotBlank
    private String password;
    @NotBlank
    private String nickname;

    public UserRegisterForm toUserRegisterForm() {
        return UserRegisterForm.builder()
                .userId(userId)
                .password(password)
                .nickname(nickname)
                .build();
    }

}
