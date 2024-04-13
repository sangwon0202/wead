package sangwon.wead.controller.DTO;

import lombok.Builder;
import lombok.Data;
import sangwon.wead.service.DTO.CommentInfo;

import java.time.format.DateTimeFormatter;

@Data
public class CommentView {
    private Long commentId;
    private String nickname;
    private String content;
    private String uploadDate;
    private boolean permission;

    @Builder
    public CommentView(CommentInfo commentInfo, String userId, String nickname) {
        this.commentId = commentInfo.getCommentId();
        this.nickname = nickname;
        this.content = commentInfo.getContent();
        this.uploadDate = commentInfo.getUploadDate().format(DateTimeFormatter.ofPattern("yyyy년 MM월 dd일"));
        this.permission = commentInfo.getUserId().equals(userId);
    }

}
