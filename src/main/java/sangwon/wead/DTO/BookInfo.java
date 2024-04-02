package sangwon.wead.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import sangwon.wead.API.BookResponse;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
public class BookInfo {
    private String isbn;
    private String title;
    private String image;
    private List<String> authors;
    private LocalDate pubdate;

    public BookInfo(BookResponse.Item item) {
        this.isbn = item.getIsbn();
        this.title = item.getTitle();
        this.image = item.getImage();
        this.authors = Arrays.stream(item.getAuthor().split("\\^")).toList();
        this.pubdate = LocalDate.parse(item.getPubdate(),DateTimeFormatter.ofPattern("yyyyMMdd"));
    }

}
