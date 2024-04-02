package sangwon.wead.controller.model;

import lombok.Data;
import sangwon.wead.DTO.BookInfo;

import java.time.LocalDate;

@Data
public class BookInfoModel {
    private String isbn;
    private String title;
    private String image;
    private String author;
    private LocalDate pubdate;

    public BookInfoModel(BookInfo bookDto) {
        this.isbn = bookDto.getIsbn();
        this.title = bookDto.getTitle();
        this.image = bookDto.getImage();
        this.author = "";
        for(String author : bookDto.getAuthors()) {
            this.author += ", " + author;
        }
        this.author = this.author.substring(2);
        this.pubdate = bookDto.getPubdate();
    }
}
