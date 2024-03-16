package sangwon.wead.DTO;

import lombok.Data;

import java.util.Date;

@Data
public class PostMetaDataDto {

    private int postId;
    private String userId;
    private String nickname;
    private String title;
    private Date uploadDate;
    private int commentNumber;
    private int views;

}
