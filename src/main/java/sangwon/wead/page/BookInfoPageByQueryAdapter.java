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
    public Page<?> getPage(Pageable pageable, Map<String, Object> args) {
        String query = (String)args.get("query");
        if(query == null) throw new RuntimeException("쿼리가 전달되지 않았습니다.");
        return bookSearchService.getBookInfoPageByQuery(pageable, query);
    }
}
