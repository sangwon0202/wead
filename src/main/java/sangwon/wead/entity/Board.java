package sangwon.wead.entity;

import lombok.Data;

import java.util.Date;

@Data
public class Board {

    private int boardId;
    private String userId;
    private String title;
    private String content;
    private Date uploadDate;

    private int view;
}
