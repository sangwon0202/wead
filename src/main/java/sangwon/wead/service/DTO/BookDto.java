package sangwon.wead.service.DTO;

import lombok.Value;
import sangwon.wead.API.BookResponse;
import sangwon.wead.redis.BookCache;
import sangwon.wead.repository.entity.Book;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;

@Value
public class BookDto {
    String isbn;
    String title;
    String image;
    String author;
    LocalDate pubdate;

    public BookDto(Book book) {
        this.isbn = book.getIsbn();
        this.title = book.getTitle();
        this.image = book.getImage();
        this.author = book.getAuthor();
        this.pubdate = book.getPubdate();
    }

    public Book toBook() {
        return Book.builder()
                .isbn(isbn)
                .title(title)
                .image(image)
                .author(author)
                .pubdate(pubdate)
                .build();
    }

    public BookDto(BookResponse.Item item) {
        this.isbn = item.getIsbn();
        this.title = item.getTitle();
        this.image = item.getImage();
        this.author = item.getAuthor().isBlank() ? null : Arrays.stream(item.getAuthor().split("\\^")).toList().get(0);
        this.pubdate = item.getPubdate().isBlank() ? null : LocalDate.parse(item.getPubdate(), DateTimeFormatter.ofPattern("yyyyMMdd"));
    }

    public BookDto(BookCache bookCache) {
        this.isbn = bookCache.getIsbn();
        this.title = bookCache.getTitle();
        this.image = bookCache.getImage();
        this.author = bookCache.getAuthor();
        this.pubdate = bookCache.getPubdate();
    }

}
