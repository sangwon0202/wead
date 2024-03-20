package sangwon.wead.service.DTO;


import lombok.Data;
import sangwon.wead.repository.entity.Comment;

import java.time.LocalDate;


@Data
public class CommentDto {
    private int commentId;
    private String userId;
    private int postId;
    private String content;
    private LocalDate uploadDate;

    public CommentDto(Comment comment) {
        this.commentId = comment.getCommentId();
        this.userId = comment.getUserId();
        this.postId = comment.getPostId();
        this.content = comment.getContent();
        this.uploadDate = comment.getUploadDate();
    }

}
