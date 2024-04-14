package sangwon.wead.repository;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import sangwon.wead.repository.entity.Post;


public interface PostRepository extends JpaRepository<Post, Long> {
    Page<Post> findByTitleContains(Pageable pageable, String title);
    Page<Post> findByUserId(Pageable pageable, String userId);

    @Query("select p from Post p join p.book b where b.title like %:bookTitle%")
    Page<Post> findByBookTitleContains(Pageable pageable, @Param("bookTitle") String bookTitle);

    @Query("select p from Post p join p.user u where u.nickname like %:nickname%")
    Page<Post> findByNicknameContains(Pageable pageable, @Param("nickname") String nickname);

}
