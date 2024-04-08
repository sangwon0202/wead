package sangwon.wead.service.DTO;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CommentUploadForm {

    private String userId;
    private Long postId;
    private String content;
}
