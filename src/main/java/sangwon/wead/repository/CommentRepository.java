package sangwon.wead.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sangwon.wead.repository.entity.Comment;


public interface CommentRepository extends JpaRepository<Comment, Long> {

}

