package sangwon.wead.repository.entity;

import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
public class Post {

    private int postId;
    private String userId;
    private String title;
    private String content;
    private Date uploadDate;
    private int views;
}
