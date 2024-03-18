package sangwon.wead.controller;


import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import sangwon.wead.exception.NonexistentCommentException;
import sangwon.wead.exception.NonexistentPostException;
import sangwon.wead.exception.PermissionException;
import sangwon.wead.service.CommentService;

@Controller
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;

    @PostMapping("/comment/upload/{postId}")
    public String upload(HttpServletRequest request,
                         @PathVariable("postId") int postId,
                         @RequestParam String content,
                         RedirectAttributes redirectAttributes,
                         Model model) {

        HttpSession session = request.getSession();
        String userId = (String)session.getAttribute("userId");

        // 로그인이 안되어 있을 경우
        if(userId == null) {
            model.addAttribute("message", "로그인을 먼저 해주세요.");
            return "alert";
        }

        // 댓글 내용이 비어있을 경우
        if(content.isEmpty()) {
            model.addAttribute("message", "댓글 내용을 입력해주세요.");
            model.addAttribute("redirect", "/post/" + postId);
            return "alert";
        }

        try {
            commentService.create(userId, postId, content);
            redirectAttributes.addAttribute("postId", postId);
            return "redirect:/post/{postId}";
        }
        // 게시글이 존재하지 않을 경우
        catch (NonexistentPostException e) {
            model.addAttribute("message", "존재하지 않는 게시글입니다.");
            return "alert";
        }
    }

    @PostMapping("/comment/delete/{commentId}")
    public String delete(HttpServletRequest request,
                         @PathVariable("commentId") int commentId,
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
            commentService.permissionCheck(userId, commentId);
            int postId = commentService.getPostId(commentId);
            commentService.delete(commentId);

            redirectAttributes.addAttribute("postId", postId);
            return "redirect:/post/{postId}";
        }
        // 댓글이 존재하지 않을 경우
        catch (NonexistentCommentException e) {
            model.addAttribute("message", "존재하지 않는 댓글입니다.");
            return "alert";
        }
        // 권한이 없을 경우
        catch (PermissionException e) {
            model.addAttribute("message", "권한이 없습니다.");
            return "alert";
        }
    }
}
