package sangwon.wead.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.util.UriComponentsBuilder;
import sangwon.wead.page.BookInfoPageByQueryAdapter;
import sangwon.wead.page.PageFactory;
import sangwon.wead.resolover.annotation.Referer;
import sangwon.wead.controller.DTO.BookLine;
import sangwon.wead.service.DTO.BookInfo;
import sangwon.wead.controller.DTO.PageBar;
import sangwon.wead.service.book.search.BookSearchService;


import java.util.Optional;

import static sangwon.wead.util.AlertPageRedirector.redirectAlertPage;

@Controller
@RequiredArgsConstructor
public class BookController {

    private final BookSearchService bookSearchService;


    @GetMapping( "/books")
    public String bookSearch(@RequestParam(value = "page", required = false, defaultValue = "1") int pageNumber,
                             @RequestParam(value = "query", required = false) String query,
                             @Referer String referer,
                             Model model) {

        String searchQuery = query == null ? "독서" : query;

        // 쿼리가 빈 문자열일 경우
        if(searchQuery.isBlank()) return redirectAlertPage("검색어를 입력해주세요.", referer, model);
        // 쿼리가 50자를 넘는 경우
        if(searchQuery.length() > 50) return redirectAlertPage("검색어는 최대 50자입니다.", referer, model);

        // 책 리스트
        Page<BookInfo> page = PageFactory.builder()
                .pageAdapter(new BookInfoPageByQueryAdapter(bookSearchService))
                .pageNumber(pageNumber)
                .pageSize(10)
                .query(searchQuery)
                .build().getPage(BookInfo.class);

        // url
        String url = UriComponentsBuilder.fromUriString("/books")
                .queryParamIfPresent("query", Optional.ofNullable(query))
                .build()
                .toUriString();

        // 책 리스트
        model.addAttribute("bookList", page.getContent().stream().map(BookLine::new));
        model.addAttribute("pageBar", new PageBar(page, 10, url));
        model.addAttribute("query", query);

        return "page/book-search";
    }

}
