package sangwon.wead.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import sangwon.wead.exception.NonexistentPostException;
import sangwon.wead.exception.PermissionException;
import sangwon.wead.service.PostService;
import sangwon.wead.service.CommentService;



@Controller
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;
    private final CommentService commentService;


    @GetMapping("/post/{postId}")
    public String read(@PathVariable("postId") int postId,
                       Model model) {

        try {
            model.addAttribute("post", postService.read(postId));
            model.addAttribute("comment", commentService.getCommentList(postId));
            postService.increaseViews(postId);
            return "page/board";
        }
        catch (NonexistentPostException e) {
            model.addAttribute("message", "존재하지 않는 게시물입니다.");
            return "alert";
        }
    }

    @GetMapping("/post/upload")
    public String uploadForm(HttpServletRequest request, Model model) {

        HttpSession session = request.getSession();
        String userId = (String)session.getAttribute("userId");

        // 로그인이 안되어 있을 경우
        if(userId == null) {
            model.addAttribute("message", "로그인을 먼저 해주세요.");
            return "alert";
        }

        model.addAttribute("action", "/post/upload");
        return "page/upload";
    }

    @PostMapping("/post/upload")
    public String upload(HttpServletRequest request,
                         @RequestParam("title") String title,
                         @RequestParam("content") String content,
                         Model model) {

        HttpSession session = request.getSession();
        String userId = (String)session.getAttribute("userId");

        // 로그인이 안되어 있을 경우
        if(userId == null) {
            model.addAttribute("message", "로그인을 먼저 해주세요.");
            return "alert";
        }

        // 제목을 입력하지 않은 경우
        if(title.isEmpty()) {
            model.addAttribute("message", "제목을 입력해주세요.");
            return "alert";
        }

        // 내용을 입력하지 않은 경우
        if(content.isEmpty()) {
            model.addAttribute("message", "내용을 입력해주세요.");
            return "alert";
        }

        postService.create(userId, title, content);
        return "redirect:/";
    }

    @GetMapping("/post/update/{postId}")
    public String updateForm(HttpServletRequest request,
                             @PathVariable("postId") int postId,
                             Model model) {

        HttpSession session = request.getSession();
        String userId = (String)session.getAttribute("userId");

        // 로그인이 안되어 있을 경우
        if(userId == null) {
            model.addAttribute("message", "로그인을 먼저 해주세요.");
            return "alert";
        }

        try {
            postService.permissionCheck(userId, postId);
            model.addAttribute("post", postService.read(postId));
            model.addAttribute("action", "/post/update/" + postId);
            return "page/upload";
        }
        // 게시글이 존재하지 않는 경우
        catch (NonexistentPostException e) {
            model.addAttribute("message", "존재하지 않는 게시물입니다.");
            return "alert";
        }
        // 게시글 접근 권한이 없을 경우
        catch (PermissionException e) {
            model.addAttribute("message", "권한이 없습니다.");
            model.addAttribute("redirect", "/post/" + postId);
            return "alert";
        }

    }

    @PostMapping("/post/update/{postId}")
    public String update(HttpServletRequest request,
                         @PathVariable("postId") int postId,
                         @RequestParam("title") String title,
                         @RequestParam("content") String content,
                         RedirectAttributes redirectAttributes,
                         Model model) {

        HttpSession session = request.getSession();
        String userId = (String)session.getAttribute("userId");

        // 로그인이 안되어 있을 경우
        if(userId == null) {
            model.addAttribute("message", "로그인을 먼저 해주세요.");
            return "alert";
        }

        try {

            postService.permissionCheck(userId, postId);

            // 제목을 입력하지 않은 경우
            if(title.isEmpty()) {
                model.addAttribute("message", "제목을 입력해주세요.");
                return "alert";
            }

            // 내용을 입력하지 않은 경우
            if(content.isEmpty()) {
                model.addAttribute("message", "내용을 입력해주세요.");
                return "alert";
            }

            postService.update(postId, title, content);
            redirectAttributes.addAttribute("postId", postId);
            return "redirect:/post/{postId}";

        }
        // 게시글이 존재하지 않는 경우
        catch (NonexistentPostException e) {
            model.addAttribute("message", "존재하지 않는 게시물입니다.");
            return "alert";
        }
        // 게시글 접근 권한이 없을 경우
        catch (PermissionException e) {
            model.addAttribute("message", "권한이 없습니다.");
            model.addAttribute("redirect", "/post/" + postId);
            return "alert";
        }

    }

    @PostMapping("/post/delete/{postId}")
    public String delete(HttpServletRequest request,
                             @PathVariable("postId") int postId,
                             Model model) {

        HttpSession session = request.getSession();
        String userId = (String)session.getAttribute("userId");

        // 로그인이 안되어 있을 경우
        if(userId == null) {
            model.addAttribute("message", "로그인을 먼저 해주세요.");
            return "alert";
        }

        try {
            postService.permissionCheck(userId, postId);
            commentService.deleteAllByPostId(postId);
            postService.delete(postId);
            return "redirect:/";
        }
        // 게시글이 존재하지 않는 경우
        catch (NonexistentPostException e) {
            model.addAttribute("message", "존재하지 않는 게시물입니다.");
            return "alert";
        }
        // 게시글 접근 권한이 없을 경우
        catch (PermissionException e) {
            model.addAttribute("message", "권한이 없습니다.");
            model.addAttribute("redirect", "/post/" + postId);
            return "alert";
        }
    }

}
