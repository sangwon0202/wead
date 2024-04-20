package sangwon.wead.service.book.search;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import sangwon.wead.service.DTO.BookDto;
import sangwon.wead.service.DTO.BookRowDto;
import sangwon.wead.exception.BookSearchFailException;


import java.util.ArrayList;

@Service
public class MockBookSearchService implements BookSearchService {

    @Override
    public BookDto getBook(String isbn) {
        throw new BookSearchFailException();
    }

    @Override
    public Page<BookRowDto> getBookByQuery(Pageable pageable, String query) {
        return new PageImpl<>(new ArrayList<>(), pageable, 0);
    }

}

