package sangwon.wead.service.book;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import sangwon.wead.exception.NonexistentBookException;
import sangwon.wead.service.DTO.BookInfo;


import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;

@Service
public class OneBookService implements BookService {
    private BookInfo bookDto;

    public OneBookService() {
        List<String> authors = new ArrayList<>();
        authors.add("이일민");
        LocalDate pubDate = LocalDate.of(2012, Month.SEPTEMBER, 21);

        this.bookDto = BookInfo.builder()
                .isbn("9788960773417")
                .title("토비의 스프링 3.1 Vol 1: 스프링의 이해와 원리 (스프링의 이해와 원리)")
                .image("https://shopping-phinf.pstatic.net/main_3246336/32463364883.20231004072332.jpg")
                .authors(authors)
                .pubdate(pubDate)
                .build();
    }


    @Override
    public BookInfo getBookInfo(String isbn) {
        if(!bookDto.getIsbn().equals(isbn)) throw new NonexistentBookException();
        else return bookDto;
    }

    @Override
    public Page<BookInfo> getBookInfoPageByQuery(String query, int pageNumber, int pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        List<BookInfo> bookInfoList = new ArrayList<>();
        if(pageNumber == 0) bookInfoList.add(bookDto);
        return new PageImpl<BookInfo>(bookInfoList, pageable, 1);
    }

}


