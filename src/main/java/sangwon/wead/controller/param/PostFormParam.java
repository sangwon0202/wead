package sangwon.wead.controller.param;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;
import sangwon.wead.service.DTO.PostUpdateForm;
import sangwon.wead.service.DTO.PostUploadForm;

@Data
@Builder
public class PostFormParam {
    @NotBlank
    private String title;
    @NotBlank
    private String content;

    public PostUpdateForm toPostUpdateForm(Long postId) {
        return PostUpdateForm.builder()
                .postId(postId)
                .title(title)
                .content(content)
                .build();
    }

    public PostUploadForm toPostUploadForm(String userId, String isbn) {
        return PostUploadForm.builder()
                .userId(userId)
                .title(title)
                .content(content)
                .isbn(isbn)
                .build();
    }

}
