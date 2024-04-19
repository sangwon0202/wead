package sangwon.wead.service.book.search;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import sangwon.wead.service.DTO.BookDto;
import sangwon.wead.service.DTO.BookRowDto;

public interface BookSearchService {

    BookDto getBook(String isbn);
    Page<BookRowDto> getBookByQuery(Pageable pageable, String query);
}

