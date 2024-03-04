package sangwon.wead.DTO;

import lombok.Data;

import java.util.Date;

@Data
public class BoardMetaDataDto {

    private int boardId;
    private String userId;
    private String nickname;
    private String title;
    private Date uploadDate;

    private int commentNumber;

}
