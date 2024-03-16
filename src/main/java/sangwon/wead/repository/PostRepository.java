package sangwon.wead.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import sangwon.wead.entity.Post;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class PostRepository {


    private final JdbcTemplate jdbcTemplate;

    public List<Post> findAll() {
        List<Post> result = jdbcTemplate.query("select * from post", postRowMapper());
        return result;
    }

    public List<Post> findPaging(int pageNumber, int count) {
        int offset = (pageNumber-1)*count;
        List<Post> result = jdbcTemplate.query("select * from post ORDERS LIMIT ? OFFSET ?", postRowMapper(), count,offset);
        return result;
    }

    public int getCount() {
        int result = jdbcTemplate.queryForObject("select count(*) from post", Integer.class);
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
                    post.getView(),
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
                    post.getView()
            );
        }
    }

    public void deleteByPostId(int postId) {
        jdbcTemplate.update("delete from post where post_id = ?", postId);
    }

    private RowMapper<Post> postRowMapper() {
        return (rs, rowNum) -> {
            Post post = new Post();
            post.setPostId(rs.getInt("post_id"));
            post.setUserId(rs.getString("user_id"));
            post.setTitle(rs.getString("title"));
            post.setContent(rs.getString("content"));
            post.setUploadDate(rs.getDate("upload_date"));
            post.setView(rs.getInt("views"));
            return post;
        };
    }

}
