package sangwon.wead.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import sangwon.wead.resolover.annotation.Referer;
import sangwon.wead.service.DTO.BookRowDto;
import sangwon.wead.service.DTO.ListDto;
import sangwon.wead.service.ListService;
import sangwon.wead.service.book.search.BookSearchService;

import static sangwon.wead.util.AlertPageRedirector.redirectAlertPage;


@Controller
@RequiredArgsConstructor
public class BookController {

    private final BookSearchService bookSearchService;
    private final ListService listService;

    @GetMapping( "/books")
    public String bookSearch(@RequestParam(value = "page", required = false, defaultValue = "1") int pageNumber,
                             @RequestParam(value = "query", required = false, defaultValue = "") String query,
                             @Referer String referer,
                             Model model) {
        if(query.length() > 50) return redirectAlertPage("검색어는 최대 50자입니다.", referer, model);

        ListDto<BookRowDto> listDto = listService.builder((pageable -> bookSearchService.getBookByQuery(pageable, query)))
                .setUrl("/books")
                .setQuery("query", query)
                .build(PageRequest.of(pageNumber, 10));

        model.addAttribute("list", listDto.getList());
        model.addAttribute("pageBar", listDto.getPageBar());
        return "page/book_search";
    }
}

