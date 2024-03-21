package sangwon.wead.controller.model;

import lombok.Data;
import sangwon.wead.service.DTO.BookDto;

import java.time.LocalDate;

@Data
public class BookLineModel {

    private String isbn;
    private String title;
    private String image;
    private String author;
    private String publisher;
    private LocalDate pubdate;

    public BookLineModel(BookDto bookDto) {
        this.isbn = bookDto.getIsbn();
        this.title = bookDto.getTitle();
        this.image = bookDto.getImage();
        this.author = bookDto.getAuthor();
        this.publisher = bookDto.getPublisher();
        this.pubdate = bookDto.getPubdate();
    }
}
