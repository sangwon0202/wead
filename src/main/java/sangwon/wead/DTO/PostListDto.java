package sangwon.wead.DTO;


import lombok.Data;

import java.util.List;

@Data
public class PostListDto {
    private List<PostMetaDataDto> postMetaDataDtoList;
    private PageBarDto pageBarDto;
}
