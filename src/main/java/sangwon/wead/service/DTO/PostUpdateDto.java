package sangwon.wead.service.DTO;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class PostUpdateDto {
    Long postId;
    String title;
    String content;

}
