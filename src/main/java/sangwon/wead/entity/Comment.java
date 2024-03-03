package sangwon.wead.entity;

import lombok.Data;

import java.util.Date;

@Data
public class Comment {

    private int commentId;
    private int boardId;
    private String userId;
    private String content;
    private Date uploadDate;
}
