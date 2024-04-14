package sangwon.wead.page;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import sangwon.wead.service.book.search.BookSearchService;

import java.util.Map;

@RequiredArgsConstructor
public class BookInfoPageByQueryAdapter implements PageAdapter {

    private final BookSearchService bookSearchService;

    @Override
    public Page<?> getPage(Pageable pageable, String query) {
        return bookSearchService.getBookInfoPageByQuery(pageable, query);
    }
}
