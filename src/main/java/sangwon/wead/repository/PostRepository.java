package sangwon.wead.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import sangwon.wead.repository.entity.Post;


public interface PostRepository extends JpaRepository<Post, Long> {

}
