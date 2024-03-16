package sangwon.wead.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import sangwon.wead.DTO.PostDto;
import sangwon.wead.DTO.PostFormDto;
import sangwon.wead.DTO.PageBarDto;
import sangwon.wead.service.PostService;
import sangwon.wead.service.CommentService;
import sangwon.wead.service.UserService;

import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class PostController {

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
        model.addAttribute("list", postService.getListPaging(pageNumber,postCountPerPage));

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

    @GetMapping("/post/upload")
    public String uploadForm(HttpServletRequest request, Model model) {

        HttpSession session = request.getSession();

        // 로그인이 안되어 있을 경우
        if(session.getAttribute("userId") == null) {
            model.addAttribute("message", "로그인을 먼저 해주세요.");
            return "alert";
        }

        model.addAttribute("action", "/post/upload");
        return "page/upload";
    }

    @PostMapping("/post/upload")
    public String upload(HttpServletRequest request,
                         @RequestParam String title,
                         @RequestParam String content,
                         Model model) {

        HttpSession session = request.getSession();

        // 로그인이 안되어 있을 경우
        if(session.getAttribute("userId") == null) {
            model.addAttribute("message", "로그인을 먼저 해주세요.");
            return "alert";
        }

        // 빈칸이 있을 경우
        if(title.equals("") || content.equals("")) {
            model.addAttribute("message", "제목 또는 내용을 입력해주세요.");
            model.addAttribute("redirect", "/post/upload");
            return "alert";
        }

        String userId = (String)session.getAttribute("userId");
        PostFormDto postFormDto = new PostFormDto();
        postFormDto.setTitle(title);
        postFormDto.setContent(content);
        postService.create(userId, postFormDto);
        return "redirect:/";
    }

    @GetMapping("/post/update/{postId}")
    public String updateForm(HttpServletRequest request,
                             @PathVariable("postId") int postId,
                             Model model) {

        HttpSession session = request.getSession();

        // 로그인이 안되어 있을 경우
        if(session.getAttribute("userId") == null) {
            model.addAttribute("message", "로그인을 먼저 해주세요.");
            return "alert";
        }

        // 게시글 존재 확인
        if(!postService.postExist(postId)) {
            model.addAttribute("message", "존재하지 않는 게시물입니다.");
            return "alert";
        };

        String userId = (String)session.getAttribute("userId");
        // 게시글 수정 권한 확인
        if(!postService.isWriter(userId, postId)) {
            model.addAttribute("message", "권한이 없습니다.");
            model.addAttribute("redirect", "/post/" + postId);
            return "alert";
        };

        PostDto postDto = postService.read(postId);
        model.addAttribute("action", "/post/update/" + postId);
        model.addAttribute("post", postDto);
        return "page/upload";
    }

    @PostMapping("/post/update/{postId}")
    public String update(HttpServletRequest request,
                             @PathVariable("postId") int postId,
                             @RequestParam("title") String title,
                             @RequestParam("content") String content,
                             RedirectAttributes redirectAttributes,
                             Model model) {

        HttpSession session = request.getSession();

        // 로그인이 안되어 있을 경우
        if(session.getAttribute("userId") == null) {
            model.addAttribute("message", "로그인을 먼저 해주세요.");
            return "alert";
        }

        // 게시글 존재 확인
        if(!postService.postExist(postId)) {
            model.addAttribute("message", "존재하지 않는 게시물입니다.");
            return "alert";
        };

        String userId = (String)session.getAttribute("userId");
        // 게시글 수정 권한 확인
        if(!postService.isWriter(userId, postId)) {
            model.addAttribute("message", "권한이 없습니다.");
            model.addAttribute("redirect", "/post/" + postId);
            return "alert";
        };

        // 빈칸이 있을 경우
        if(title.equals("") || content.equals("")) {
            model.addAttribute("message", "제목 또는 내용을 입력해주세요.");
            model.addAttribute("redirect", "/post/update/" + postId);
            return "alert";
        }

        PostFormDto postFormDto = new PostFormDto();
        postFormDto.setTitle(title);
        postFormDto.setContent(content);
        postService.update(postId, postFormDto);

        redirectAttributes.addAttribute("postId", postId);
        return "redirect:/post/{postId}";
    }

    @PostMapping("/post/delete/{postId}")
    public String delete(HttpServletRequest request,
                             @PathVariable("postId") int postId,
                             Model model) {

        HttpSession session = request.getSession();

        // 로그인이 안되어 있을 경우
        if(session.getAttribute("userId") == null) {
            model.addAttribute("message", "로그인을 먼저 해주세요.");
            return "alert";
        }

        // 게시글 존재 확인
        if(!postService.postExist(postId)) {
            model.addAttribute("message", "존재하지 않는 게시물입니다.");
            return "alert";
        };

        String userId = (String)session.getAttribute("userId");
        // 게시글 수정 권한 확인
        if(!postService.isWriter(userId, postId)) {
            model.addAttribute("message", "권한이 없습니다.");
            model.addAttribute("redirect", "/post/" + postId);
            return "alert";
        };

        commentService.deleteAllByPostId(postId);
        postService.delete(postId);
        return "redirect:/";
    }

}
