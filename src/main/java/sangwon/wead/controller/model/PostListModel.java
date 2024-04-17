package sangwon.wead.controller.model;

import lombok.Builder;
import lombok.Value;
import sangwon.wead.service.DTO.PostInfo;

import java.util.List;

@Builder
@Value
public class PostListModel {
    List<Post> postList;
    PageBarModel pageBarModel;
    SearchBarModel searchBarModel;

    @Builder
    @Value
    static public class Post {
        Long postId;
        String nickname;
        String title;
        String uploadDate;
        int views;
        int commentCount;
        String image;

    }
}
