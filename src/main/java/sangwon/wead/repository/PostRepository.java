package sangwon.wead.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import sangwon.wead.repository.entity.Post;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class PostRepository {


    private final JdbcTemplate jdbcTemplate;

    public int getCount() {
        int result = jdbcTemplate.queryForObject("select count(*) from post", Integer.class);
        return result;
    }

    public List<Post> findAll(int pageNumber, int count) {
        int offset = (pageNumber-1)*count;
        List<Post> result = jdbcTemplate.query("select * from post ORDERS LIMIT ? OFFSET ?", postRowMapper(), count,offset);
        return result;
    }

    public Optional<Post> findByPostId(int postId) {
        List<Post> result = jdbcTemplate.query("select * from post where post_id = ?", postRowMapper(), postId);
        return result.stream().findAny();
    }

    public void save(Post post) {
        Optional<Post> optionalPost = this.findByPostId(post.getPostId());

        // update
        if(optionalPost.isPresent()) {
            jdbcTemplate.update("update post set user_id = ?, title = ?, content = ?, upload_date = ?, views = ? " +
                    "where post_id = ?",
                    post.getUserId(),
                    post.getTitle(),
                    post.getContent(),
                    post.getUploadDate(),
                    post.getViews(),
                    post.getPostId()
                    );
        }
        // insert
        else {
            jdbcTemplate.update("insert post(user_id, title, content, upload_date, views) values(?, ?, ?, ?, ?)",
                    post.getUserId(),
                    post.getTitle(),
                    post.getContent(),
                    post.getUploadDate(),
                    post.getViews()
            );
        }
    }

    public void deleteByPostId(int postId) {
        jdbcTemplate.update("delete from post where post_id = ?", postId);
    }

    private RowMapper<Post> postRowMapper() {
        return (rs, rowNum) ->
                Post.builder()
                        .postId(rs.getInt("post_id"))
                        .userId(rs.getString("user_id"))
                        .title(rs.getString("title"))
                        .content(rs.getString("content"))
                        .uploadDate(rs.getDate("upload_date"))
                        .views(rs.getInt("views"))
                        .build();
    }

}
