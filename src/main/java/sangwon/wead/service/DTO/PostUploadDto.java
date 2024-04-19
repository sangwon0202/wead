package sangwon.wead.service.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
@AllArgsConstructor
public class PostUploadDto {
    String userId;
    String title;
    String content;
    String isbn;

}
