package sangwon.wead.service.DTO;

import lombok.Builder;
import lombok.Data;
import sangwon.wead.repository.entity.Comment;

import java.util.Date;


@Data
public class CommentDto {
    private int commentId;
    private String userId;
    private int postId;
    private String content;
    private Date uploadDate;

    public CommentDto(Comment comment) {
        this.commentId = comment.getCommentId();
        this.userId = comment.getUserId();
        this.postId = comment.getPostId();
        this.content = comment.getContent();
        this.uploadDate = comment.getUploadDate();
    }

}
