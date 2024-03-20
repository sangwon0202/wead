package sangwon.wead.controller.model;


import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;


@Data
@Builder
public class CommentModel {
    private int commentId;
    private String nickname;
    private String content;
    private LocalDate uploadDate;
    private boolean permission;
}
