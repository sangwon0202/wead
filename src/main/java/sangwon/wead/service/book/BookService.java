package sangwon.wead.service.book;

import org.springframework.data.domain.Page;
import sangwon.wead.DTO.BookInfo;

public interface BookService {

    BookInfo getBookInfo(String isbn);
    Page<BookInfo> getBookInfoPageByQuery(String query, int pageNumber, int pageSize);
}
