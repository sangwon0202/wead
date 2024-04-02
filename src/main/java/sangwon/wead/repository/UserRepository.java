package sangwon.wead.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import sangwon.wead.repository.entity.User;

public interface UserRepository extends JpaRepository<User, String> {
}
