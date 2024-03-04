package sangwon.wead.DTO;

import lombok.Data;

import java.util.Date;

@Data
public class CommentDto {

    private int commentId;
    private String userId;
    private String nickname;
    private String content;
    private Date uploadDate;
}
