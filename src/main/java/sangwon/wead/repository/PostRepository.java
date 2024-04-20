package sangwon.wead.repository;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import sangwon.wead.repository.entity.Post;


public interface PostRepository extends JpaRepository<Post, Long> {


    @Query("select p from Post p join fetch p.user join fetch p.book")
    Page<Post> findAllFetchJoin(Pageable pageable);

    @Query("select p from Post p join fetch p.user u join fetch p.book " +
            "where u.userId like %:userId%")
    Page<Post> findByUserIdFetchJoin(Pageable pageable, @Param("userId") String userId);

    @Query("select p from Post p join fetch p.user join fetch p.book " +
            "where p.title like %:title%")
    Page<Post> findByTitleContainsFetchJoin(Pageable pageable, @Param("title") String title);

    @Query("select p from Post p join fetch p.user join fetch p.book b " +
            "where b.title like %:bookTitle%")
    Page<Post> findByBookTitleContainsFetchJoin(Pageable pageable, @Param("bookTitle") String bookTitle);

    @Query("select p from Post p join fetch p.user u join fetch p.book " +
            "where u.nickname like %:nickname%")
    Page<Post> findByNicknameContainsFetchJoin(Pageable pageable, @Param("nickname") String nickname);

    int countByUserUserId(String userId);

}
