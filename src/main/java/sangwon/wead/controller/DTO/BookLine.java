package sangwon.wead.controller.DTO;

import lombok.Data;
import sangwon.wead.service.DTO.BookInfo;

import java.time.format.DateTimeFormatter;


@Data
public class BookLine {

    private String isbn;
    private String title;
    private String image;
    private String authors;
    private String pubdate;

    public BookLine(BookInfo bookInfo) {
        this.isbn = bookInfo.getIsbn();
        this.title = bookInfo.getTitle();
        this.image = bookInfo.getImage();
        this.authors = bookInfo.getAuthors().get(0);
        if(bookInfo.getAuthors().size() > 1) this.authors += " 외 " + Integer.toString(bookInfo.getAuthors().size()-1) + "명";
        if(bookInfo.getPubdate() == null) this.pubdate = "정보 없음";
        else this.pubdate = bookInfo.getPubdate().format(DateTimeFormatter.ofPattern("yyyy년 MM월 dd일"));
    }
}
