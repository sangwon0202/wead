package sangwon.wead.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import sangwon.wead.page.BookInfoPageByQueryAdapter;
import sangwon.wead.page.PageAdjuster;
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
    public String bookSearch(@RequestParam(value = "query", required = false) Optional<String> optionalQuery,
                             @RequestParam(value = "page", required = false, defaultValue = "1") int pageNumber,
                             @Referer String referer,
                             Model model) {
        // 쿼리가 전달되었을 경우
        if(optionalQuery.isPresent()) {
            String query = optionalQuery.get();

            // 쿼리가 빈 문자열일 경우
            if(query.trim().isBlank()) return redirectAlertPage("검색어를 입력해주세요.", referer, model);
            // 쿼리가 50자를 넘는 경우
            if(query.length() > 50) return redirectAlertPage("검색어는 최대 50자입니다.", referer, model);

            Page<BookInfo> page = PageAdjuster
                    .pageAdapter(new BookInfoPageByQueryAdapter(bookSearchService))
                    .arg("query", query)
                    .getPage(pageNumber, 10, BookInfo.class);

            // 책 리스트
            model.addAttribute("bookList", page.getContent().stream().map(BookLine::new));
            model.addAttribute("pageBar", new PageBar(page,10));
            model.addAttribute("query", query);
        }

        return "page/book-search";
    }

}
