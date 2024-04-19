package sangwon.wead.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import sangwon.wead.repository.entity.Comment;


public interface CommentRepository extends JpaRepository<Comment, Long> {

    @Query("select c from Comment c join fetch c.user " +
            "where c.post.postId = :postId")
    Page<Comment> findByPostIdFetchJoin(Pageable pageable, @Param("postId") Long postId);

}

