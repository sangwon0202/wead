package sangwon.wead.service.book.search;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import sangwon.wead.API.naver.NaverAPIBookResponse;
import sangwon.wead.API.naver.NaverAPIBookClient;
import sangwon.wead.service.DTO.BookInfo;

import java.util.ArrayList;
import java.util.List;

@Primary
@Service
@RequiredArgsConstructor
public class NaverBookSearchService implements BookSearchService {
    private final NaverAPIBookClient NaverAPIbookClient;


    @Override
    public boolean checkBookExistence(String isbn) {
        try {
            getBookInfo(isbn);
            return true;
        }
        catch(Exception e) {
            return false;
        }
    }

    @Override
    public BookInfo getBookInfo(String isbn) {
        return NaverAPIbookClient.searchBook(isbn,10, 1, "sim")
                .getItems()
                .stream()
                .filter(item -> item.getIsbn().equals(isbn))
                .findAny()
                .orElseThrow(() -> new RuntimeException("isbn 에 해당하는 책이 존재하지 않습니다."))
                .toBookInfo();
    }

    @Override
    public Page<BookInfo> getBookInfoPageByQuery(Pageable pageable, String query) {
        int pageNumber = pageable.getPageNumber();
        int pageSize = pageable.getPageSize();
        if(pageSize > 100) throw new IllegalArgumentException("The page size should not exceed 100.");

        int total = NaverAPIbookClient.searchBook(query,10, 1, "sim").getTotal();
        if(total > 1000) total = 1000;

        int start = pageNumber * pageSize + 1;
        List<BookInfo> bookInfoList;
        if(start > total) bookInfoList = new ArrayList<>();
        else {
            int display;
            int available = total - start + 1;
            if(pageSize <= available) display = pageSize;
            else display = available;
            ;
            bookInfoList = NaverAPIbookClient.searchBook(query, display, start, "sim")
                    .getItems()
                    .stream()
                    .map(NaverAPIBookResponse.Item::toBookInfo).toList();
        }

        return new PageImpl<>(bookInfoList, pageable, total);
    }

}
