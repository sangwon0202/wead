package sangwon.wead.controller.model;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class PostLineModel {
    private int postId;
    private String title;
    private int commentCount;
    private String nickname;
    private LocalDate uploadDate;
    private int views;

}
