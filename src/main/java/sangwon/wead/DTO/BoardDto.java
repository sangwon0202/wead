package sangwon.wead.DTO;

import lombok.Data;

import java.util.Date;

@Data
public class BoardDto {

    private int boardId;
    private String userId;
    private String nickname;
    private String title;
    private String content;
    private Date uploadDate;

}
