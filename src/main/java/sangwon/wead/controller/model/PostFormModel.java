package sangwon.wead.controller.model;

import lombok.Builder;
import lombok.Value;
import sangwon.wead.service.DTO.BookInfo;

@Builder
@Value
public class PostFormModel {
    boolean update;
    boolean alert;
    Book book;
    Long postId;
    String title;
    String content;

    @Value
    static public class Book {
        String isbn;
        String image;
        String title;
        String author;

        @Builder
        public Book(BookInfo bookInfo) {
            this.isbn = bookInfo.getIsbn();
            this.image = bookInfo.getImage();
            this.title = bookInfo.getTitle();
            this.author = bookInfo.getAuthor();
        }
    }
}
