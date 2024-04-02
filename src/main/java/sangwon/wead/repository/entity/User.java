package sangwon.wead.repository.entity;


import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User {
    @Id
    @Column(name = "user_id", length = 320)
    private String id;
    @Column(name = "password", length = 20)
    private String password;
    @Column(name = "nickname", length = 10)
    private String nickname;

    @Builder
    public User(String id, String password, String nickname) {
        this.id = id;
        this.password = password;
        this.nickname = nickname;
    }
}
