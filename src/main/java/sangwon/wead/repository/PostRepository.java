package sangwon.wead.repository;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import sangwon.wead.repository.entity.Post;


public interface PostRepository extends JpaRepository<Post, Long> {
    Page<Post> findByTitleContains(String title, Pageable pageable);

}
