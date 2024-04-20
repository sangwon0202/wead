package sangwon.wead.service.DTO;

import lombok.Value;
import sangwon.wead.repository.entity.Post;

@Value
public class PostSimpleRowDto {
    String title;
    Long postId;

    public PostSimpleRowDto(Post post) {
        this.title = post.getTitle();
        this.postId = post.getPostId();
    }
}
