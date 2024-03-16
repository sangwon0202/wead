package sangwon.wead.DTO;

import lombok.Data;

import java.util.Date;

@Data
public class PostDto {

    private int postId;
    private String userId;
    private String nickname;
    private String title;
    private String content;
    private Date uploadDate;
    private int views;

}
