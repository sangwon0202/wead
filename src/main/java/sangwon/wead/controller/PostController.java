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
    public String post(@PathVariable("postId") int postId,
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
                         @RequestParam String title,
                         @RequestParam String content,
                         Model model) {

        HttpSession session = request.getSession();
        String userId = (String)session.getAttribute("userId");

        // 로그인이 안되어 있을 경우
        if(userId == null) {
            model.addAttribute("message", "로그인을 먼저 해주세요.");
            return "alert";
        }

        // 빈칸이 있을 경우
        if(title.equals("") || content.equals("")) {
            model.addAttribute("message", "제목 또는 내용을 입력해주세요.");
            model.addAttribute("redirect", "/post/upload");
            return "alert";
        }

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
        String userId = (String)session.getAttribute("userId");

        // 로그인이 안되어 있을 경우
        if(userId == null) {
            model.addAttribute("message", "로그인을 먼저 해주세요.");
            return "alert";
        }

        try {
            postService.permissionCheck(userId, postId);

            PostDto postDto = postService.read(postId);
            model.addAttribute("action", "/post/update/" + postId);
            model.addAttribute("post", postDto);
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
