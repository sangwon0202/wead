package sangwon.wead.controller.DTO;

import lombok.Builder;
import lombok.Data;
import sangwon.wead.service.DTO.PostInfo;

import java.time.format.DateTimeFormatter;


@Data
public class PostView {
    private Long postId;
    private String nickname;
    private String title;
    private String content;
    private String uploadDate;
    private int views;
    private int commentCount;
    private boolean permission;

    @Builder
    public PostView(PostInfo postInfo, String userId, String nickname, int commentCount) {
        this.postId = postInfo.getPostId();
        this.nickname = nickname;
        this.title = postInfo.getTitle();
        this.content = postInfo.getContent();
        this.uploadDate = postInfo.getUploadDate().format(DateTimeFormatter.ofPattern("yyyy년 MM월 dd일"));
        this.views = postInfo.getViews();
        this.commentCount = commentCount;
        this.permission = postInfo.getUserId().equals(userId);
    }
}
