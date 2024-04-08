package sangwon.wead.service.DTO;

import lombok.Builder;
import lombok.Data;


@Data
@Builder
public class PostUploadForm {
    private String userId;
    private String title;
    private String content;
    private String isbn;
}
