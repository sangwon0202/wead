package sangwon.wead.controller.model;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class PostModel {
    private int postId;
    private String title;
    private String nickname;
    private LocalDate uploadDate;
    private int views;
    private boolean permission;
    private String content;
}
