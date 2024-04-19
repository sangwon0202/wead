package sangwon.wead.service.DTO;
import lombok.Value;
import sangwon.wead.repository.entity.Post;

import java.time.LocalDate;

@Value
public class PostDto {
    Long postId;
    String userId;
    String title;
    String content;
    LocalDate uploadDate;
    int views;

    public PostDto(Post post) {
        this.postId = post.getPostId();
        this.userId = post.getUser().getUserId();
        this.title = post.getTitle();
        this.content = post.getContent();
        this.uploadDate = post.getUploadDate();
        this.views = post.getViews();
    }
}
