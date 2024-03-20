package sangwon.wead.repository.entity;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.Date;


@Data
@Builder
public class Comment {
    private int commentId;
    private int postId;
    private String userId;
    private String content;
    private LocalDate uploadDate;
}
