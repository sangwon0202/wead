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
                .description("대한민국 전자정부 표준 프레임워크 스프링을 설명하는 No. 1 베스트셀러!\n" +
                        "\n" +
                        "단순한 예제를 스프링 3.0과 스프링 3.1의 기술을 적용하며 발전시켜 나가는 과정을 통해 스프링의 핵심 프로그래밍 모델인 IoC/DI, PSA, AOP의 원리와 이에 적용된 다양한 디자인 패턴, 프로그래밍 기법을 이해할 수 있도록 도와준다. 이어지는 에서 상세히 소개하는 스프링 3.0과 스프링 3.1의 방대한 기술을 쉽게 이해하고 효과적으로 응용하는 데 필요한 기반 지식을 쌓도록 도와준다. \n" +
                        "\n" +
                        "『토비의 스프링 3.1』은 스프링을 처음 접하거나 스프링을 경험했지만 스프링이 어렵게 느껴지는 개발자부터 스프링을 활용한 아키텍처를 설계하고 프레임워크를 개발하려고 하는 아키텍트에 이르기까지 모두 참고할 수 있는 스프링 완벽 바이블이다.")
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
