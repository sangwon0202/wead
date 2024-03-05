package sangwon.wead.controller;


import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import sangwon.wead.service.BoardService;
import sangwon.wead.service.CommentService;
import sangwon.wead.service.UserService;

@Controller
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;
    private final UserService userService;
    private final CommentService commentService;

    @GetMapping("/board")
    public String main(HttpServletRequest request, Model model) {

        // 로그인 정보
        HttpSession session = request.getSession();
        String userId = (String)session.getAttribute("userId");
        if(userId != null) model.addAttribute("user", userService.getUserInfo(userId));

        model.addAttribute("list", boardService.getListAll());
        return "main";
    }

    @GetMapping("/board/{boardId}")
    public String board(@PathVariable("boardId") int boardId,
                        HttpServletRequest request,
                        Model model) {

        // 로그인 정보
        HttpSession session = request.getSession();
        String userId = (String)session.getAttribute("userId");
        if(userId != null) model.addAttribute("user", userService.getUserInfo(userId));

        // 게시글 존재 확인
        if(!boardService.boardExist(boardId)) {
            model.addAttribute("message", "존재하지 않는 게시물입니다.");
            return "error";
        };

        model.addAttribute("board",boardService.read(boardId));
        model.addAttribute("comments", commentService.getCommentList(boardId));
        model.addAttribute("list", boardService.getListAll());
        return "main";
    }


}
