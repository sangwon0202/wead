package sangwon.wead.DTO;


import lombok.Data;
import sangwon.wead.repository.entity.Comment;

import java.time.LocalDate;


@Data
public class CommentInfo {
    private Long commentId;
    private Long postId;
    private String userId;
    private String nickname;
    private String content;
    private LocalDate uploadDate;

    public CommentInfo(Comment comment) {
        this.commentId = comment.getId();
        this.postId = comment.getPost().getId();
        this.userId = comment.getUser().getId();
        this.nickname = comment.getUser().getNickname();
        this.content = comment.getContent();
        this.uploadDate = comment.getUploadDate();
    }

}
