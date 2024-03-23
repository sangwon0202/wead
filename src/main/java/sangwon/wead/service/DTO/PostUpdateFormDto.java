package sangwon.wead.service.DTO;

import lombok.Data;
import sangwon.wead.repository.entity.Post;

@Data
public class PostUpdateFormDto {
    private String title;
    private String content;
    private String isbn;

    public PostUpdateFormDto(Post post) {
        this.title = post.getTitle();
        this.content = post.getContent();
        this.isbn = post.getIsbn();
    }
}
