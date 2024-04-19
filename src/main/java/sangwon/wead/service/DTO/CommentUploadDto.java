package sangwon.wead.service.DTO;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class CommentUploadDto {
    String userId;
    Long postId;
    String content;
}
