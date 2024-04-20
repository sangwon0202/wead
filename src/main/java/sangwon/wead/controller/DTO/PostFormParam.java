package sangwon.wead.controller.DTO;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;
import sangwon.wead.service.DTO.PostUpdateDto;
import sangwon.wead.service.DTO.PostUploadDto;

@Data
@Builder
public class PostFormParam {
    @NotBlank
    private String title;
    @NotBlank
    private String content;

    public PostUploadDto toPostUploadDto(String userId, String isbn) {
        return PostUploadDto.builder()
                .userId(userId)
                .title(title)
                .content(content)
                .isbn(isbn)
                .build();
    }

    public PostUpdateDto toPostUpdateDto(Long postId) {
        return PostUpdateDto.builder()
                .postId(postId)
                .title(title)
                .content(content)
                .build();
    }

}
