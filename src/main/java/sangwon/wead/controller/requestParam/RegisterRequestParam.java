package sangwon.wead.controller.requestParam;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import sangwon.wead.service.DTO.RegisterDto;

@Data
public class RegisterRequestParam {
    @NotBlank(message = "아이디를 입력해주세요.")
    private String userId;
    @NotBlank(message = "비밀번호를 입력해주세요.")
    private String password;
    @NotBlank(message = "닉네임을 입력해주세요.")
    private String nickname;

    public RegisterDto toRegisterDto() {
        return RegisterDto.builder()
                .userId(this.userId)
                .password(this.password)
                .nickname(this.nickname)
                .build();
    }
}
