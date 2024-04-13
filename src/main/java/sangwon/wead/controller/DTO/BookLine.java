package sangwon.wead.controller.DTO;

import lombok.Data;
import sangwon.wead.service.DTO.BookInfo;

import java.time.format.DateTimeFormatter;


@Data
public class BookLine {

    private String isbn;
    private String title;
    private String image;
    private String author;
    private String pubdate;

    public BookLine(BookInfo bookInfo) {
        this.isbn = bookInfo.getIsbn();
        this.title = bookInfo.getTitle();
        this.image = bookInfo.getImage(); // 이미지 없을 경우 처리해야함
        this.author = bookInfo.getAuthor() == null ? "작가 정보 없음" : bookInfo.getAuthor();
        this.pubdate = bookInfo.getPubdate() == null ? "출판일 정보 없음" : bookInfo.getPubdate().format(DateTimeFormatter.ofPattern("yyyy년 MM월 dd일"));
    }
}
