package sangwon.wead.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import sangwon.wead.controller.DTO.BookLine;
import sangwon.wead.exception.NonexistentPageException;
import sangwon.wead.service.DTO.BookInfo;
import sangwon.wead.controller.DTO.PageBar;
import sangwon.wead.service.UserService;
import sangwon.wead.service.book.BookService;

import java.util.Optional;

import static sangwon.wead.controller.util.AlertPageRedirector.redirectAlertPage;

@Controller
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;
    private final UserService userService;


    @GetMapping( "/book")
    public String bookSearch(@RequestParam(value = "query", required = false) Optional<String> optionalQuery,
                             @RequestParam(value = "page", required = false, defaultValue = "1") int pageNumber,
                           HttpServletRequest request,
                           Model model) {

        // 로그인 정보
        HttpSession session = request.getSession();
        String userId = (String)session.getAttribute("userId");
        if(userId != null) model.addAttribute("nickname", userService.getUserInfo(userId).getNickname());

        // 이전 URL
        String referer = request.getHeader("referer");

        // 쿼리가 전달되었을 경우
        if(optionalQuery.isPresent()) {
            String query = optionalQuery.get();
            // 쿼리가 빈 문자열일 경우
            if(query.trim().isBlank()) return redirectAlertPage("검색어를 입력해주세요.", referer, model);
            if(query.length() > 50) return redirectAlertPage("검색어는 최대 50자입니다.", referer, model);

            // 페이지가 범위를 벗어날 경우 재조정
            if(pageNumber < 1) pageNumber = 1;
            Page<BookInfo> page = bookService.getBookInfoPageByQuery(query, pageNumber-1, 10);
            int totalPages = page.getTotalPages();
            if(totalPages == 0) totalPages = 1;
            if(pageNumber > totalPages) page = bookService.getBookInfoPageByQuery(query, totalPages - 1, 10);

            model.addAttribute("bookList", page.getContent().stream().map(bookinfo -> new BookLine(bookinfo)));
            model.addAttribute("pageBar", new PageBar(page,10));
            model.addAttribute("query", query);
        }

        return "page/book-search";
    }

}
