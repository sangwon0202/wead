package sangwon.wead.service.DTO;

import lombok.Data;

@Data
public class PostUpdateDto {
    private String userId;
    private int postId;
    private String title;
    private String content;
}
