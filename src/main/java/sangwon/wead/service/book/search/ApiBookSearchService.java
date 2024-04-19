package sangwon.wead.service.book.search;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import sangwon.wead.API.BookClient;
import sangwon.wead.service.DTO.BookDto;
import sangwon.wead.service.DTO.BookRowDto;
import sangwon.wead.service.exception.NonExistentBookException;

import java.util.ArrayList;
import java.util.List;

@Primary
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
                .orElseThrow(NonExistentBookException::new);
    }

    @Override
    public Page<BookRowDto> getBookByQuery(Pageable pageable, String query) {
        if(query == null || query.isBlank()) return new PageImpl<>(new ArrayList<>(), pageable, 0);

        int pageNumber = pageable.getPageNumber();
        int pageSize = pageable.getPageSize();
        if(pageSize > 100) throw new IllegalArgumentException("페이지의 크기는 100 이하여야합니다.");

        int total = bookClient.searchBook(query,10, 1, "sim").getTotal();
        if(total > 1000) total = 1000;
        int start = pageNumber * pageSize + 1;
        if(start > total) return new PageImpl<>(new ArrayList<>(), pageable, total);

        int display;
        int available = total - start + 1;
        display = Math.min(pageSize, available);

        List<BookRowDto> content = bookClient.searchBook(query, display, start, "sim")
                .getItems()
                .stream()
                .map(BookRowDto::new)
                .toList();

        return new PageImpl<>(content, pageable, total);
    }

}


