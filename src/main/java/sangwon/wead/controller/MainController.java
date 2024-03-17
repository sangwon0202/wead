package sangwon.wead.controller;


import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import sangwon.wead.DTO.PostListDto;
import sangwon.wead.exception.NonexistentPageException;
import sangwon.wead.service.PostService;
import sangwon.wead.service.UserService;


@Controller
@RequiredArgsConstructor
public class MainController {

    private final PostService postService;
    private final UserService userService;

    private final int postCountPerPage = 10;
    private final int pageCountPerPageBar = 10;

    @GetMapping("/")
    public String post(@RequestParam(value = "page", required = false, defaultValue = "1") int pageNumber,
                       HttpServletRequest request,
                       Model model) {

        // 로그인 정보
        HttpSession session = request.getSession();
        String userId = (String)session.getAttribute("userId");
        if(userId != null) model.addAttribute("user", userService.getUserInfo(userId));

        // 게시글 리스트
        try {
            PostListDto postListDto = postService.getList(pageNumber,postCountPerPage, pageCountPerPageBar);
            model.addAttribute("list", postListDto.getPostMetaDataDtoList());
            model.addAttribute("pageBar", postListDto.getPageBarDto());
            return "page/main";
        }
        catch (NonexistentPageException e) {
            model.addAttribute("message", "존재하지 않는 페이지입니다.");
            return "alert";
        }
    }
}
