package sangwon.wead.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import sangwon.wead.repository.entity.Comment;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class CommentRepository {

    private final JdbcTemplate jdbcTemplate;

    public List<Comment> findAllByPostId(int postId) {
        List<Comment> result = jdbcTemplate.query("select * from comment where post_id = ?", commentRowMapper(),postId);
        return result;
    }

    public Optional<Comment> findByCommentId(int commentId) {
        List<Comment> result = jdbcTemplate.query("select * from comment where comment_id = ?", commentRowMapper(), commentId);
        return result.stream().findAny();
    }

    public void save(Comment comment) {
        Optional<Comment> optionalComment = this.findByCommentId(comment.getCommentId());

        // update
        if(optionalComment.isPresent()) {
            jdbcTemplate.update("update comment set post_id = ?, user_id = ?, content = ?, upload_date = ? " +
                            "where comment_id = ?",
                    comment.getPostId(),
                    comment.getUserId(),
                    comment.getContent(),
                    comment.getUploadDate(),
                    comment.getCommentId()
            );
        }
        // insert
        else {
            jdbcTemplate.update("insert comment(post_id, user_id, content, upload_date) " +
                            "values(?, ?, ?, ?)",
                    comment.getPostId(),
                    comment.getUserId(),
                    comment.getContent(),
                    comment.getUploadDate()
            );
        }
    }

    public void deleteByCommentId(int commentId) {
        jdbcTemplate.update("delete from comment where comment_id = ?", commentId);
    }

    public void deleteAllByPostId(int postId) {
        jdbcTemplate.update("delete from comment where post_id = ?", postId);
    }


    private RowMapper<Comment> commentRowMapper() {
        return (rs, rowNum) ->
                Comment.builder()
                    .commentId(rs.getInt("comment_id"))
                    .postId(rs.getInt("post_id"))
                    .userId(rs.getString("user_id"))
                    .content(rs.getString("content"))
                    .uploadDate(rs.getDate("upload_date").toLocalDate())
                    .build();
    }
}

