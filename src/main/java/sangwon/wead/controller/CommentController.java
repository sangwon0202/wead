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
import sangwon.wead.service.PostService;
import sangwon.wead.service.CommentService;

@Controller
@RequiredArgsConstructor
public class CommentController {

    private final PostService postService;
    private final CommentService commentService;

    @PostMapping("/comment/upload/{postId}")
    public String upload(HttpServletRequest request,
                         @PathVariable("postId") int postId,
                         @RequestParam String content,
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
        commentService.create(userId, postId, content);

        redirectAttributes.addAttribute("postId", postId);
        return "redirect:/post/{postId}";
    }

    @PostMapping("/comment/delete/{commentId}")
    public String delete(HttpServletRequest request,
                         @PathVariable("commentId") int commentId,
                         RedirectAttributes redirectAttributes,
                         Model model) {

        HttpSession session = request.getSession();

        // 로그인이 안되어 있을 경우
        if(session.getAttribute("userId") == null) {
            model.addAttribute("message", "로그인을 먼저 해주세요.");
            return "alert";
        }

        // 댓글 존재 확인
        if(!commentService.commentExist(commentId)) {
            model.addAttribute("message", "존재하지 않는 댓글입니다.");
            return "alert";
        };

        String userId = (String)session.getAttribute("userId");
        // 게시글 수정 권한 확인
        if(!commentService.isWriter(userId, commentId)) {
            model.addAttribute("message", "권한이 없습니다.");
            return "alert";
        };

        int postId = commentService.getPostId(commentId);
        commentService.delete(commentId);

        redirectAttributes.addAttribute("postId", postId);
        return "redirect:/post/{postId}";
    }




}