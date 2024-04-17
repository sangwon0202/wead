package sangwon.wead.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import sangwon.wead.controller.model.builder.ModelBuilder;
import sangwon.wead.page.BookInfoPageByQueryAdapter;
import sangwon.wead.page.PageFactory;
import sangwon.wead.resolover.annotation.Referer;
import sangwon.wead.service.DTO.BookInfo;
import sangwon.wead.service.book.search.BookSearchService;


import static sangwon.wead.util.AlertPageRedirector.redirectAlertPage;

@Controller
@RequiredArgsConstructor
public class BookController {

    private final BookSearchService bookSearchService;
    private final ModelBuilder modelBuilder;

    @GetMapping( "/books")
    public String bookSearch(@RequestParam(value = "page", required = false, defaultValue = "1") int pageNumber,
                             @RequestParam(value = "query", required = false) String query,
                             @Referer String referer,
                             Model model) {

        String searchQuery = query == null ? "독서" : query;

        if(searchQuery.isBlank()) return redirectAlertPage("검색어를 입력해주세요.", referer, model);
        if(searchQuery.length() > 50) return redirectAlertPage("검색어는 최대 50자입니다.", referer, model);

        Page<BookInfo> page = PageFactory.builder()
                .pageAdapter(new BookInfoPageByQueryAdapter(bookSearchService))
                .pageNumber(pageNumber)
                .pageSize(10)
                .query(searchQuery)
                .build().getPage(BookInfo.class);

        model.addAttribute("sectionModel",modelBuilder.buildBookSearchModel(page, query));
        return "page/book_search";
    }

}
