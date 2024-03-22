package sangwon.wead.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import sangwon.wead.controller.model.BookLineModel;
import sangwon.wead.controller.model.PageBarModel;
import sangwon.wead.controller.model.UserInfoModel;
import sangwon.wead.exception.APICallFailException;
import sangwon.wead.exception.NonexistentPageException;
import sangwon.wead.exception.NonexistentUserException;
import sangwon.wead.service.BookService;
import sangwon.wead.service.DTO.BookDto;
import sangwon.wead.service.UserService;

import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class BookController {
    private final BookService bookService;
    private final UserService userService;
    private final int countPerPage = 10;
    private final int pageCountPerPageBar = 10;

    @GetMapping("/book")
    public String bookSearch(@RequestParam(value = "query", required = false) Optional<String> optionalQuery,
                             @RequestParam(value = "page", required = false, defaultValue = "1") int pageNumber,
                           HttpServletRequest request,
                           Model model) {

        // 로그인 정보
        HttpSession session = request.getSession();
        String userId = (String)session.getAttribute("userId");

        try { if(userId != null) model.addAttribute("userInfo", new UserInfoModel(userService.getUser(userId))); }
        // 존재하지 않는 회원인 경우 (회원 탈퇴했으나 세션이 동기화 안된 경우)
        catch (NonexistentUserException e) {
            session.removeAttribute("userId");
            return "redirect:/";
        }

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


        // 게시글 리스트 및 페이지바
        try {
            model.addAttribute("bookList", buildBookList(bookService.getBookList(query, pageNumber, countPerPage)));
            model.addAttribute("pageBar",new PageBarModel(bookService.getPageBar(query, pageNumber, countPerPage, pageCountPerPageBar)));
            return "page/book-search";
        }
        catch (APICallFailException e) {
            model.addAttribute("message", "책 검색 시스템을 불러올 수 없습니다.");
            return "alert";
        }
        // 해당 페이지가 존재하지 않을 경우
        catch (NonexistentPageException e) {
            model.addAttribute("message", "존재하지 않는 페이지입니다.");
            return "alert";
        }
    }

    private List<BookLineModel> buildBookList(List<BookDto> bookDtoList) {
        return bookDtoList.stream().map((bookDto -> new BookLineModel(bookDto))).toList();
    }

}
