package sangwon.wead.DTO;

import lombok.Data;
import sangwon.wead.repository.entity.Post;

@Data
public class PostUploadForm {
    private String title;
    private String content;
    private String isbn;
}
