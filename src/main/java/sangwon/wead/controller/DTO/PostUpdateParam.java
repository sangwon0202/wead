package sangwon.wead.controller.DTO;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import sangwon.wead.service.DTO.PostUpdateForm;

@Data
public class PostUpdateParam {
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

}
