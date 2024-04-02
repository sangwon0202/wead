package sangwon.wead.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import sangwon.wead.DTO.BookInfo;
import sangwon.wead.DTO.PageBar;
import sangwon.wead.service.UserService;
import sangwon.wead.service.book.BookService;

import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;
    private final UserService userService;



    @GetMapping("/book")
    public String bookSearch(@RequestParam(value = "query", required = false) Optional<String> optionalQuery,
                             @RequestParam(value = "page", required = false, defaultValue = "1") int pageNumber,
                           HttpServletRequest request,
                           Model model) {

        // 로그인 정보
        HttpSession session = request.getSession();
        String userId = (String)session.getAttribute("userId");
        if(userId != null) model.addAttribute("userInfo", userService.getUserInfo(userId));

        // 쿼리가 전달되지 않을 경우, '독서' 키워드로 검색
        String query = "독서";
        if(optionalQuery.isPresent()) {
            query = optionalQuery.get();
            model.addAttribute("query", query);
        }

        // 쿼리가 빈 문자열일 경우
        if(query.trim().isBlank()) {
            model.addAttribute("message", "검색어를 입력해주세요.");
            model.addAttribute("redirect", "/book");
            return "alert";
        }

        pageNumber -= 1;
        Page<BookInfo> page = bookService.getBookInfoPageByQuery(query, pageNumber, 10);
        model.addAttribute("bookInfoList", page.getContent());
        model.addAttribute("pageBar", new PageBar(page,10));
        return "page/book-search";
    }

}
