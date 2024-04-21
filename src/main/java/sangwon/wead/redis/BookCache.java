package sangwon.wead.redis;

import lombok.Value;
import sangwon.wead.API.BookResponse;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;

@Value
public class BookCache implements Serializable {
    String isbn;
    String title;
    String image;
    String author;
    LocalDate pubdate;
    public BookCache(BookResponse.Item item) {
        this.isbn = item.getIsbn();
        this.title = item.getTitle();
        this.image = item.getImage();
        this.author = item.getAuthor().isBlank() ? null : Arrays.stream(item.getAuthor().split("\\^")).toList().get(0);
        this.pubdate = item.getPubdate().isBlank() ? null : LocalDate.parse(item.getPubdate(), DateTimeFormatter.ofPattern("yyyyMMdd"));
    }
}
