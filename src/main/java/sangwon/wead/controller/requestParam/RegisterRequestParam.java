package sangwon.wead.controller.requestParam;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import sangwon.wead.service.DTO.UserRegisterDto;

@Data
public class RegisterRequestParam {
    @NotBlank(message = "아이디를 입력해주세요.")
    private String userId;
    @NotBlank(message = "비밀번호를 입력해주세요.")
    private String password;
    @NotBlank(message = "닉네임을 입력해주세요.")
    private String nickname;

    public UserRegisterDto toRegisterDto() {
        return UserRegisterDto.builder()
                .userId(this.userId)
                .password(this.password)
                .nickname(this.nickname)
                .build();
    }
}
