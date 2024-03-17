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
import sangwon.wead.service.PostService;
import sangwon.wead.service.CommentService;
import sangwon.wead.service.UserService;


@Controller
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;
    private final UserService userService;
    private final CommentService commentService;



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
