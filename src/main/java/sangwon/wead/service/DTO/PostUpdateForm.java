package sangwon.wead.service.DTO;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PostUpdateForm {
    private Long postId;
    private String title;
    private String content;
}
