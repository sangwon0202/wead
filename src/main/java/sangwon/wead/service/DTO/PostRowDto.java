package sangwon.wead.service.DTO;


import lombok.Value;
import sangwon.wead.repository.entity.Post;

import java.time.LocalDate;

@Value
public class PostRowDto {
    Long postId;
    String title;
    String nickname;
    LocalDate uploadDate;
    int views;
    int commentCount;
    String image;

    public PostRowDto(Post post, int commentCount) {
        this.postId = post.getPostId();
        this.title = post.getTitle();
        this.nickname = post.getUser().getNickname();
        this.uploadDate = post.getUploadDate();
        this.views = post.getViews();
        this.commentCount = commentCount;
        this.image = post.getBook().getImage();
    }

}
