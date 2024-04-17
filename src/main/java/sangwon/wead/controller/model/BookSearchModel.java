package sangwon.wead.controller.model;

import lombok.Builder;
import lombok.Value;

import java.util.List;

@Builder
@Value
public class BookSearchModel {
    List<Book> bookList;
    PageBarModel pageBarModel;
    SearchBarModel searchBarModel;

    @Builder
    @Value
    static public class Book {
        String isbn;
        String title;
        String image;
        String author;
        String pubdate;
    }
}
