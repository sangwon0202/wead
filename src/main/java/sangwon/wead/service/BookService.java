package sangwon.wead.service;

import sangwon.wead.exception.APICallFailException;
import sangwon.wead.exception.NonexistentBookException;
import sangwon.wead.exception.NonexistentPageException;
import sangwon.wead.service.DTO.BookDto;
import sangwon.wead.service.DTO.PageBarDto;

import java.util.List;

public interface BookService {

    BookDto getBook(String isbn) throws APICallFailException, NonexistentBookException;
    List<BookDto> getBookList(String query, int pageNumber, int countPerPage) throws APICallFailException, NonexistentPageException;
    PageBarDto getPageBar(String query, int pageNumber, int countPerPage, int pageCountPerPageBar) throws APICallFailException, NonexistentPageException;
}
