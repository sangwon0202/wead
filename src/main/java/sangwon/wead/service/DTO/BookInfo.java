package sangwon.wead.service.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import sangwon.wead.repository.entity.Book;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
public class BookInfo {
    private String isbn;
    private String title;
    private String image;
    private String author;
    private LocalDate pubdate;

    public Book toBook() {
        return Book.builder()
                .isbn(this.isbn)
                .title(this.title)
                .image(this.image)
                .author(this.author)
                .pubdate(this.pubdate)
                .build();
    }

    public BookInfo(Book book) {
        this.isbn = book.getIsbn();
        this.title = book.getTitle();
        this.image = book.getImage();
        this.author = book.getAuthor();
        this.pubdate = book.getPubdate();
    }

}
