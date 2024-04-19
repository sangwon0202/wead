package sangwon.wead.service.DTO;

import lombok.Value;
import sangwon.wead.repository.entity.Comment;

import java.time.LocalDate;

@Value
public class CommentRowDto {
    Long commentId;
    String userId;
    String nickname;
    String content;
    LocalDate uploadDate;

    public CommentRowDto(Comment comment) {
        this.commentId = comment.getCommentId();
        this.userId = comment.getUser().getUserId();
        this.nickname = comment.getUser().getNickname();
        this.content = comment.getContent();
        this.uploadDate = comment.getUploadDate();
    }
}
