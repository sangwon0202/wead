package sangwon.wead.service.DTO;

import lombok.Value;
import sangwon.wead.API.BookResponse;
import sangwon.wead.redis.BookCache;
import sangwon.wead.repository.entity.Book;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;

@Value
public class BookRowDto {
    String isbn;
    String title;
    String image;
    String author;
    LocalDate pubdate;

    public BookRowDto(BookResponse.Item item) {
        this.isbn = item.getIsbn();
        this.title = item.getTitle();
        this.image = item.getImage();
        this.author = item.getAuthor().isBlank() ? null : Arrays.stream(item.getAuthor().split("\\^")).toList().get(0);
        this.pubdate = item.getPubdate().isBlank() ? null : LocalDate.parse(item.getPubdate(), DateTimeFormatter.ofPattern("yyyyMMdd"));
    }

    public BookRowDto(BookCache bookCache) {
        this.isbn = bookCache.getIsbn();
        this.title = bookCache.getTitle();
        this.image = bookCache.getImage();
        this.author = bookCache.getAuthor();
        this.pubdate = bookCache.getPubdate();
    }


}