package sangwon.wead.service.book.search;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import sangwon.wead.service.DTO.BookInfo;

public interface BookSearchService {

    boolean checkBookExistence(String isbn);
    BookInfo getBookInfo(String isbn);
    Page<BookInfo> getBookInfoPageByQuery(Pageable pageable, String query);
}
