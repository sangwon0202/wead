package sangwon.wead.controller.DTO;

import lombok.Data;
import sangwon.wead.service.DTO.BookInfo;

@Data
public class BookView {
    private String isbn;
    private String title;
    private String image;
    private String author;

    public BookView(BookInfo bookInfo) {
        this.isbn = bookInfo.getIsbn();
        this.title = bookInfo.getTitle();
        this.image = bookInfo.getImage(); // 이미지 없을 경우 처리해야함
        this.author = bookInfo.getAuthor() == null ? "작가 정보 없음" : bookInfo.getAuthor();
    }
}
