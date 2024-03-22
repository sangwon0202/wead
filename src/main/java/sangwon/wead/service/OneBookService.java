package sangwon.wead.service;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import sangwon.wead.exception.NonexistentBookException;
import sangwon.wead.exception.NonexistentPageException;
import sangwon.wead.service.DTO.BookDto;
import sangwon.wead.service.DTO.PageBarDto;
import sangwon.wead.service.util.PageUtil;

import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;

@Service
@Primary
public class OneBookService implements BookService {
    private BookDto bookDto;

    public OneBookService() {
        List<String> authors = new ArrayList<>();
        authors.add("이일민");
        LocalDate pubDate = LocalDate.of(2012, Month.SEPTEMBER, 21);

        this.bookDto = BookDto.builder()
                .isbn("9788960773417")
                .title("토비의 스프링 3.1 Vol 1: 스프링의 이해와 원리 (스프링의 이해와 원리)")
                .image("https://shopping-phinf.pstatic.net/main_3246336/32463364883.20231004072332.jpg")
                .authors(authors)
                .pubdate(pubDate)
                .build();
    }

    @Override
    public BookDto getBook(String isbn) throws NonexistentBookException {
        if(!bookDto.getIsbn().equals(isbn)) throw new NonexistentBookException();
        else return bookDto;
    }

    @Override
    public List<BookDto> getBookList(String query, int pageNumber, int countPerPage) throws NonexistentPageException {
        if(pageNumber != 1) throw new NonexistentPageException();
        List<BookDto> bookDtoList = new ArrayList<>();
        bookDtoList.add(bookDto);
        return bookDtoList;
    }

    @Override
    public PageBarDto getPageBar(String query, int pageNumber, int countPerPage, int pageCountPerPageBar) throws NonexistentPageException {
        if(pageNumber != 1) throw new NonexistentPageException();
        return PageUtil.buildPageBar(1, 1, countPerPage, pageCountPerPageBar);
    }
}
