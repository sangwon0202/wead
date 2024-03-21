package sangwon.wead.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import sangwon.wead.exception.APICallFailException;
import sangwon.wead.exception.NonexistentBookException;
import sangwon.wead.exception.NonexistentPageException;
import sangwon.wead.naverAPI.NaverAPIBookClient;
import sangwon.wead.service.DTO.BookDto;
import sangwon.wead.service.DTO.PageBarDto;
import sangwon.wead.service.util.PageUtil;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookService {

    private final NaverAPIBookClient naverAPIBookClient;

    public BookDto getBook(String isbn) throws APICallFailException, NonexistentBookException {
        return naverAPIBookClient.search(isbn,10, 1, "sim")
                .getItems()
                .stream()
                .filter(item -> item.getIsbn().equals(isbn))
                .map(item -> new BookDto(item))
                .findAny()
                .orElseThrow(() -> new NonexistentBookException());
    }

    public List<BookDto> getBookList(String query, int pageNumber, int countPerPage) throws APICallFailException,NonexistentPageException {
        int count = naverAPIBookClient.search(query,1, 1, "sim").getTotal();
        if(count > 1000) count = 1000;
        if(!PageUtil.checkPageExistence(count, pageNumber, countPerPage)) throw new NonexistentPageException();

        int start = (pageNumber-1)*pageNumber + 1;
        return naverAPIBookClient.search(query,countPerPage, start, "sim")
                .getItems()
                .stream()
                .map(item -> new BookDto(item)).toList();
    }

    public PageBarDto getPageBar(String query, int pageNumber, int countPerPage, int pageCountPerPageBar) throws APICallFailException, NonexistentPageException {
        int count = naverAPIBookClient.search(query,1, 1, "sim").getTotal();
        if(count > 1000) count = 1000;
        if(!PageUtil.checkPageExistence(count, pageNumber, countPerPage)) throw new NonexistentPageException();
        return PageUtil.buildPageBar(count, pageNumber, countPerPage, pageCountPerPageBar);
    }

}
