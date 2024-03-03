package sangwon.wead.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import sangwon.wead.entity.User;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class UserRepository {

    private final JdbcTemplate jdbcTemplate;
    public Optional<User> findByUserId(String userId) {
        List<User> result = jdbcTemplate.query("select * from user where user_id = ?", userRowMapper(), userId);
        return result.stream().findAny();
    }

    public void save(User user) {
        Optional<User> optionalUser = this.findByUserId(user.getUserId());

        // update
        if(optionalUser.isPresent()) {
            jdbcTemplate.update("update user set password = ?, nickname = ? " +
                            "where user_id = ?",
                    user.getPassword(),
                    user.getNickname(),
                    user.getUserId()
            );
        }
        // insert
        else {
            jdbcTemplate.update("insert user(user_id, password, nickname) values(?, ?, ?)",
                    user.getUserId(),
                    user.getPassword(),
                    user.getNickname()
            );
        }
    }

    private RowMapper<User> userRowMapper() {
        return (rs, rowNum) -> {
            User user = new User();
            user.setUserId(rs.getString("user_id"));
            user.setPassword(rs.getString("password"));
            user.setNickname(rs.getString("nickname"));
            return user;
        };
    }

}
