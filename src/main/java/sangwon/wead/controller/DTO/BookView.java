package sangwon.wead.controller.DTO;

import lombok.Data;
import sangwon.wead.service.DTO.BookInfo;

@Data
public class BookView {
    private String isbn;
    private String title;
    private String image;
    private String authors;

    public BookView(BookInfo bookInfo) {
        this.isbn = bookInfo.getIsbn();
        this.title = bookInfo.getTitle();
        this.image = bookInfo.getImage();
        if(bookInfo.getAuthors() == null) this.authors = "작가 정보 없음";
        else {
            this.authors = bookInfo.getAuthors().get(0);
            if(bookInfo.getAuthors().size() > 1) this.authors += " 외 " + (bookInfo.getAuthors().size()-1) + "명";
        }
    }
}
