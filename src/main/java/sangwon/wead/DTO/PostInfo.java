package sangwon.wead.DTO;

import lombok.Data;
import sangwon.wead.repository.entity.Post;

import java.time.LocalDate;

@Data
public class PostInfo {
    private Long postId;
    private String userId;
    private String nickname;
    private String title;
    private String content;
    private LocalDate uploadDate;
    private String isbn;
    private int views;
    private int commentCount;

    public PostInfo(Post post) {
        this.postId = post.getId();
        this.userId = post.getUser().getId();
        this.nickname = post.getUser().getNickname();
        this.title = post.getTitle();
        this.content = post.getContent();
        this.uploadDate = post.getUploadDate();
        this.isbn = post.getIsbn();
        this.views = post.getViews();
        this.commentCount = post.getComments().size();
    }
}
