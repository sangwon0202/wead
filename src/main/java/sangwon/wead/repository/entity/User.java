package sangwon.wead.repository.entity;


import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User {
    @Id
    private String userId;
    private String password;
    private String nickname;

    public void changePassword(String password) {
        this.password = password;
    }

}
