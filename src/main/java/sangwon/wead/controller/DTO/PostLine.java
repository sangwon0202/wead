package sangwon.wead.controller.DTO;

import lombok.Builder;
import lombok.Data;
import sangwon.wead.service.DTO.PostInfo;

import java.time.format.DateTimeFormatter;

@Data
public class PostLine {
    private Long postId;
    private String nickname;
    private String title;
    private String uploadDate;
    private int views;
    private int commentCount;
    private String image;

    public PostLine(PostInfo postInfo, String image) {
        this.postId = postInfo.getPostId();
        this.nickname = postInfo.getNickname();
        this.title = postInfo.getTitle();
        this.uploadDate = postInfo.getUploadDate().format(DateTimeFormatter.ofPattern("yyyy년 MM월 dd일"));
        this.views = postInfo.getViews();
        this.commentCount = postInfo.getCommentCount();
        this.image = image;
    }
}
