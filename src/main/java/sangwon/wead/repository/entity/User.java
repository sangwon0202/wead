package sangwon.wead.repository.entity;


import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class User {
    private String userId;
    private String password;
    private String nickname;
}
