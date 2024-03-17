package sangwon.wead.controller;


import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import sangwon.wead.DTO.PageBarDto;
import sangwon.wead.service.CommentService;
import sangwon.wead.service.PostService;
import sangwon.wead.service.UserService;

import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class MainController {

    private final PostService postService;
    private final UserService userService;
    private final CommentService commentService;

    private final int postCountPerPage = 10;
    private final int pageCountPerPageBar = 10;

    @GetMapping(value = {"/", "/post/{postId}"})
    public String post(@PathVariable(value = "postId" ,required = false) Optional<Integer> optionalPostId,
                       @RequestParam(value = "page", required = false, defaultValue = "1") int pageNumber,
                       HttpServletRequest request,
                       Model model) {

        // 로그인 정보
        HttpSession session = request.getSession();
        String userId = (String)session.getAttribute("userId");
        if(userId != null) model.addAttribute("user", userService.getUserInfo(userId));


        // 게시글 조회 시
        if(optionalPostId.isPresent()) {
            int postId = optionalPostId.get();

            // 게시글 존재 확인
            if(!postService.postExist(postId)) {
                model.addAttribute("message", "존재하지 않는 게시물입니다.");
                return "alert";
            };

            // 조회수 증가
            postService.increaseViews(postId);

            // 게시글 및 댓글 모델 전달
            model.addAttribute("post",postService.read(postId));
            model.addAttribute("comment", commentService.getCommentList(postId));
        }

        // 리스트 모델 전달
        model.addAttribute("list", postService.getList(pageNumber,postCountPerPage));

        // 페이지바 더미 데이터 전달
        PageBarDto pageBarDto = new PageBarDto();
        pageBarDto.setPrev(true);
        pageBarDto.setNext(false);
        pageBarDto.setStart(1);
        pageBarDto.setEnd(10);
        pageBarDto.setCurrent(5);
        model.addAttribute("pageBar", pageBarDto);

        return "page/main";
    }
}
