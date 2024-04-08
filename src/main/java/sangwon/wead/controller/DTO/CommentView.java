package sangwon.wead.controller.DTO;

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


    public CommentView(CommentInfo commentInfo, String userId) {
        this.commentId = commentInfo.getCommentId();
        this.nickname = commentInfo.getNickname();
        this.content = commentInfo.getContent();
        this.uploadDate = commentInfo.getUploadDate().format(DateTimeFormatter.ofPattern("yyyy년 MM월 dd일"));
        this.permission = commentInfo.getUserId().equals(userId);

    }

}
