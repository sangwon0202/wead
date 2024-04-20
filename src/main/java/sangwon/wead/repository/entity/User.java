package sangwon.wead.repository.entity;


import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User {
    @Id
    private String userId;
    private String password;
    private String nickname;
    private LocalDate registerDate;
    @Builder
    public User(String userId, String password, String nickname) {
        this.userId = userId;
        this.password = password;
        this.nickname = nickname;
        this.registerDate = LocalDate.now();
    }

    public void changePassword(String password) {
        this.password = password;
    }

}
