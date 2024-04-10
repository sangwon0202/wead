package sangwon.wead.controller;


import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttribute;
import sangwon.wead.service.DTO.CommentUploadForm;
import sangwon.wead.service.CommentService;

import static sangwon.wead.controller.util.AlertPageRedirector.redirectAlertPage;

@Controller
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;


    @PostMapping("/comment/upload/{postId}")
    public String upload(HttpServletRequest request,
                         @SessionAttribute(name = "userId", required = false) String userId,
                         @PathVariable("postId") Long postId,
                         @RequestParam("content") String content,
                         Model model) {
        // 이전 URL
        String referer = request.getHeader("referer");

        // 로그인을 하지 않은 경우
        if(userId == null) return redirectAlertPage("로그인을 먼저 해주세요.", referer, model);

        // 댓글 업로드
        CommentUploadForm commentUploadForm = CommentUploadForm.builder()
                .userId(userId)
                .postId(postId)
                .content(content)
                .build();
        commentService.uploadComment(commentUploadForm);
        return "redirect:" + referer;
    }

    @PostMapping("/comment/delete/{commentId}")
    public String delete(HttpServletRequest request,
                         @PathVariable("commentId") Long commentId) {
        // 이전 URL
        String referer = request.getHeader("referer");

        // 댓글 삭제
        commentService.deleteComment(commentId);
        return "redirect:" + referer;
    }

}
