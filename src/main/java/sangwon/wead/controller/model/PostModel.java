package sangwon.wead.controller.model;

import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
public class PostModel {
    private int postId;
    private String title;
    private String nickname;
    private Date uploadDate;
    private int views;
    private boolean permission;
    private String content;
}
