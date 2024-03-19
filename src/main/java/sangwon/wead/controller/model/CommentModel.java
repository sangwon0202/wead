package sangwon.wead.controller.model;


import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
public class CommentModel {
    private int commentId;
    private String nickname;
    private String content;
    private Date uploadDate;
    private boolean permission;
}
