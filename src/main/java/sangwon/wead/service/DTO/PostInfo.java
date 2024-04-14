package sangwon.wead.service.DTO;

import lombok.Data;
import sangwon.wead.repository.entity.Post;

import java.time.LocalDate;

@Data
public class PostInfo {
    private Long postId;
    private String userId;
    private String title;
    private String content;
    private LocalDate uploadDate;
    private String isbn;
    private int views;

    public PostInfo(Post post) {
        this.postId = post.getId();
        this.userId = post.getUser().getId();
        this.title = post.getTitle();
        this.content = post.getContent();
        this.uploadDate = post.getUploadDate();
        this.isbn = post.getBook().getIsbn();
        this.views = post.getViews();
    }
}
