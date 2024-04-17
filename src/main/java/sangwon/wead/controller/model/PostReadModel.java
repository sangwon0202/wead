package sangwon.wead.controller.model;

import lombok.Builder;
import lombok.Value;

import java.util.List;

@Builder
@Value
public class PostReadModel {

    Post post;
    Book book;
    List<Comment> commentList;


    @Builder
    @Value
    public static class Post {
        Long postId;
        String nickname;
        String title;
        String content;
        String uploadDate;
        int views;
        boolean permission;

    }
    @Builder
    @Value
    public static class Book {
        String isbn;
        String title;
        String image;
        String author;
    }
    @Builder
    @Value
    public static class Comment {
        Long commentId;
       String nickname;
        String content;
        String uploadDate;
        boolean permission;

    }
}
