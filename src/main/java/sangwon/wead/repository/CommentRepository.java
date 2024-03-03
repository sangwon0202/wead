package sangwon.wead.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import sangwon.wead.entity.Comment;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class CommentRepository {

    private final JdbcTemplate jdbcTemplate;

    public List<Comment> findAllByBoardId(int BoardId) {
        List<Comment> result = jdbcTemplate.query("select * from comment", commentRowMapper());
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
            jdbcTemplate.update("update comment set board_id = ?, user_id = ?, content = ?, upload_date = ? " +
                            "where comment_id = ?",
                    comment.getBoardId(),
                    comment.getUserId(),
                    comment.getContent(),
                    comment.getUploadDate(),
                    comment.getCommentId()
            );
        }
        // insert
        else {
            jdbcTemplate.update("insert comment(board_id, user_id, content, upload_date) " +
                            "values(?, ?, ?, ?)",
                    comment.getBoardId(),
                    comment.getUserId(),
                    comment.getContent(),
                    comment.getUploadDate()
            );
        }
    }

    public void deleteByCommentId(int commentId) {
        jdbcTemplate.update("delete from comment where comment_id = ?", commentId);
    }



    private RowMapper<Comment> commentRowMapper() {
        return (rs, rowNum) -> {
            Comment comment = new Comment();
            comment.setCommentId(rs.getInt("comment_id"));
            comment.setBoardId(rs.getInt("board_id"));
            comment.setUserId(rs.getString("user_id"));
            comment.setContent(rs.getString("content"));
            comment.setUploadDate(rs.getDate("upload_date"));
            return comment;
        };
    }

}

