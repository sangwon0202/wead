package sangwon.wead.controller.DTO;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginParam {
    @NotBlank
    String userId;
    @NotBlank
    String password;

}

