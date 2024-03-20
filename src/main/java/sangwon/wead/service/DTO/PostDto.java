package sangwon.wead.service.DTO;

import lombok.Data;
import sangwon.wead.repository.entity.Post;

import java.time.LocalDate;

@Data
public class PostDto {
    private int postId;
    private String userId;
    private String title;
    private String content;
    private LocalDate uploadDate;
    private int views;

    public PostDto(Post post) {
        this.postId = post.getPostId();
        this.userId = post.getUserId();
        this.title = post.getTitle();
        this.content = post.getContent();
        this.uploadDate = post.getUploadDate();
        this.views = post.getViews();
    }
}
