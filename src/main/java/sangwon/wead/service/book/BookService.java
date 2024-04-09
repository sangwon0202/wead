package sangwon.wead.service.book;

import org.springframework.data.domain.Page;
import sangwon.wead.service.DTO.BookInfo;

public interface BookService {

    boolean checkBookExistence(String isbn);
    BookInfo getBookInfo(String isbn);
    Page<BookInfo> getBookInfoPageByQuery(String query, int pageNumber, int pageSize);
}
