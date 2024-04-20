package sangwon.wead.controller.DTO;

import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class UserPasswordParam {
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{6,10}$")
    private String password;
}
