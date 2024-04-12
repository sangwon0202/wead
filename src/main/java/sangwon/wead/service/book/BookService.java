package sangwon.wead.service.book;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import sangwon.wead.service.DTO.BookInfo;

public interface BookService {

    boolean checkBookExistence(String isbn);
    BookInfo getBookInfo(String isbn);
    Page<BookInfo> getBookInfoPageByQuery(Pageable pageable, String query);
}
