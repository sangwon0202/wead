package sangwon.wead.repository.entity;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class Post {

    private int postId;
    private String userId;
    private String title;
    private String content;
    private LocalDate uploadDate;
    private int views;
}
