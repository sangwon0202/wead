package sangwon.wead.controller.DTO;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import sangwon.wead.service.DTO.PostUploadForm;

@Data
public class PostUploadParam {
    @NotBlank
    private String title;
    @NotBlank
    private String content;
    @NotBlank
    private String isbn;

    public PostUploadForm toPostUploadForm(String userId) {
        return PostUploadForm.builder()
                .userId(userId)
                .title(title)
                .content(content)
                .isbn(isbn)
                .build();
    }

}
