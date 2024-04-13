package sangwon.wead.service.book.search;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import sangwon.wead.service.DTO.BookInfo;


import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;

@Service
public class OneBookSearchService implements BookSearchService {
    private BookInfo bookDto;

    public OneBookSearchService() {
        LocalDate pubDate = LocalDate.of(2012, Month.SEPTEMBER, 21);
        this.bookDto = BookInfo.builder()
                .isbn("9788960773417")
                .title("토비의 스프링 3.1 Vol 1: 스프링의 이해와 원리 (스프링의 이해와 원리)")
                .image("https://shopping-phinf.pstatic.net/main_3246336/32463364883.20231004072332.jpg")
                .author("이일민")
                .pubdate(pubDate)
                .build();
    }

    @Override
    public boolean checkBookExistence(String isbn) {
        return bookDto.getIsbn().equals(isbn);
    }

    @Override
    public BookInfo getBookInfo(String isbn) {
        if(!bookDto.getIsbn().equals(isbn)) throw new RuntimeException("토비의 스프링이 아닙니다.");
        else return bookDto;
    }

    @Override
    public Page<BookInfo> getBookInfoPageByQuery(Pageable pageable, String query) {
        int pageNumber = pageable.getPageNumber();
        List<BookInfo> bookInfoList = new ArrayList<>();
        if(pageNumber == 0) bookInfoList.add(bookDto);
        return new PageImpl<>(bookInfoList, pageable, 1);
    }

}


