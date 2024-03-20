package sangwon.wead.service.DTO;


import lombok.Data;
import sangwon.wead.naverAPI.NaverAPIBookResponse;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;

@Data
public class BookDto {
    private String isbn;
    private String title;
    private String image;
    private String author;
    private String publisher;
    private LocalDate pubdate;

    public BookDto(NaverAPIBookResponse.Item item) {
        this.isbn = item.getIsbn();
        this.title = item.getTitle();
        this.image = item.getImage();
        this.author = item.getAuthor();
        this.publisher = item.getPublisher();
        this.pubdate = LocalDate.parse(item.getPubdate(),DateTimeFormatter.ofPattern("yyyyMMdd"));
    }

}
