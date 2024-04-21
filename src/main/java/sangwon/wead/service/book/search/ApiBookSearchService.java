package sangwon.wead.service.book.search;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import sangwon.wead.API.BookClient;
import sangwon.wead.API.BookResponse;
import sangwon.wead.service.DTO.BookDto;
import sangwon.wead.service.DTO.BookRowDto;
import sangwon.wead.exception.BookSearchFailException;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ApiBookSearchService implements BookSearchService {
    private final BookClient bookClient;


    @Override
    public BookDto getBook(String isbn) {
        return bookClient.searchBook(isbn,10, 1, "sim")
                .getItems()
                .stream()
                .filter(item -> item.getIsbn().equals(isbn))
                .map(BookDto::new)
                .findAny()
                .orElseThrow(BookSearchFailException::new);
    }

    @Override
    public Page<BookRowDto> getBookByQuery(Pageable pageable, String query) {
        if(query == null || query.isBlank()) return new PageImpl<>(new ArrayList<>(), pageable, 0);

        int pageNumber = pageable.getPageNumber();
        int pageSize = pageable.getPageSize();
        int start = pageNumber*pageSize;
        if(pageSize > 100) throw new IllegalArgumentException("페이지의 크기는 100 이하여야합니다.");

        BookResponse bookResponse = bookClient.searchBook(query, pageSize, Math.min(start+1, 1000), "sim");
        int total = Math.min(bookResponse.getTotal(), 1000);
        if(start >= total) return new PageImpl<>(new ArrayList<>(), pageable, total);

        int available = 1000-start;

        List<BookRowDto> content = bookResponse.getItems().stream()
                .limit(Math.min(pageSize,available))
                .map(BookRowDto::new)
                .toList();

        return new PageImpl<>(content, pageable, total);
    }

}


