package sangwon.wead.controller;


import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import sangwon.wead.DTO.CommentInfo;
import sangwon.wead.DTO.CommentUploadForm;
import sangwon.wead.service.CommentService;

@Controller
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/comment/upload/{postId}")

    public String upload(HttpServletRequest request,
                         @PathVariable("postId") Long postId,
                         @ModelAttribute CommentUploadForm commentUploadForm,
                         RedirectAttributes redirectAttributes,
                         Model model) {

        HttpSession session = request.getSession();
        String userId = (String)session.getAttribute("userId");

        // 로그인이 안되어 있을 경우
        if(userId == null) {
            model.addAttribute("message", "로그인을 먼저 해주세요.");
            return "alert";
        }

        commentService.uploadComment(userId, postId, commentUploadForm);
        redirectAttributes.addAttribute("postId", postId);
        return "redirect:/post/{postId}";
    }

    @PostMapping("/comment/delete/{commentId}")
    public String delete(HttpServletRequest request,
                         @PathVariable("commentId") Long commentId,
                         RedirectAttributes redirectAttributes,
                         Model model) {

        HttpSession session = request.getSession();
        String userId = (String)session.getAttribute("userId");

        // 로그인이 안되어 있을 경우
        if(userId == null) {
            model.addAttribute("message", "로그인을 먼저 해주세요.");
            return "alert";
        }

        CommentInfo commentInfo = commentService.getCommentInfo(commentId);

        // 수정 권한이 없는 경우
        if(!commentInfo.getUserId().equals(userId)) {
            model.addAttribute("message", "권한이 없습니다.");
            return "alert";
        }

        commentService.deleteComment(commentId);
        redirectAttributes.addAttribute("postId", commentInfo.getPostId());
        return "redirect:/post/{postId}";
    }

}
