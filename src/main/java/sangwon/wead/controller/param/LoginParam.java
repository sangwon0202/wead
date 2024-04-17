package sangwon.wead.controller.param;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginParam {
    @NotBlank
    String userId;
    @NotBlank
    String password;

}

