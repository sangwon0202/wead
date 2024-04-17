package sangwon.wead.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import sangwon.wead.resolover.annotation.Referer;
import sangwon.wead.resolover.annotation.UserId;
import sangwon.wead.service.DTO.CommentUploadForm;
import sangwon.wead.service.CommentService;
import sangwon.wead.util.AlertPageRedirector;

import static sangwon.wead.util.AlertPageRedirector.redirectAlertPage;

@Controller
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;

    @PostMapping("/posts/{postId}/comments/upload")
    public String upload(@PathVariable("postId") Long postId,
                         @RequestParam("content") String content,
                         @UserId String userId,
                         @Referer String referer,
                         Model model) {

        if(content.isBlank()) {
            return redirectAlertPage("댓글을 작성해주세요.", referer, model);
        }

        CommentUploadForm commentUploadForm = CommentUploadForm.builder()
                .userId(userId)
                .postId(postId)
                .content(content)
                .build();
        commentService.uploadComment(commentUploadForm);
        return "redirect:" + referer;
    }

    @PostMapping("/comments/{commentId}/delete")
    public String delete(@PathVariable("commentId") Long commentId,
                         @Referer String referer) {
        // 댓글 삭제
        commentService.deleteComment(commentId);
        return "redirect:" + referer;
    }

}
