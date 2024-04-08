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
        this.authors = bookInfo.getAuthors().get(0);
        if(bookInfo.getAuthors().size() > 1) this.authors += " 외 " + Integer.toString(bookInfo.getAuthors().size()-1) + "명";
    }
}
