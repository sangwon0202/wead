package sangwon.wead.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import sangwon.wead.resolover.annotation.Referer;
import sangwon.wead.resolover.annotation.UserId;
import sangwon.wead.aspect.annotation.CheckLogin;
import sangwon.wead.aspect.annotation.NeedLogin;
import sangwon.wead.exception.ClientFaultException;
import sangwon.wead.exception.NonexistentPostException;
import sangwon.wead.service.DTO.CommentUploadForm;
import sangwon.wead.service.CommentService;
import sangwon.wead.service.PostService;
import sangwon.wead.util.AlertPageRedirector;

@Controller
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;
    private final PostService postService;

    @NeedLogin
    @PostMapping("/comment/upload/{postId}")
    public String upload(@PathVariable("postId") Long postId,
                         @RequestParam("content") String content,
                         @UserId String userId,
                         @Referer String referer,
                         Model model) {
        // 게시글이 존재하지 않을 경우
        if(!postService.checkPostExistence(postId)) throw new NonexistentPostException();
        // 댓글 내용이 비었을 경우
        if(content.isBlank()) return AlertPageRedirector.redirectAlertPage("댓글을 작성해주세요.", referer, model);

        // 댓글 업로드
        CommentUploadForm commentUploadForm = CommentUploadForm.builder()
                .userId(userId)
                .postId(postId)
                .content(content)
                .build();
        commentService.uploadComment(commentUploadForm);
        return "redirect:" + referer;
    }

    @CheckLogin
    @PostMapping("/comment/delete/{commentId}")
    public String delete(@PathVariable("commentId") Long commentId,
                         @UserId String userId,
                         @Referer String referer) {
        // 존재하지 않는 댓글일 경우
        if(!commentService.checkCommentExistence(commentId)) throw new ClientFaultException();
        // 삭제 권한이 없을 경우
        if(!commentService.getCommentInfo(commentId).getUserId().equals(userId)) throw new ClientFaultException();

        // 댓글 삭제
        commentService.deleteComment(commentId);
        return "redirect:" + referer;
    }

}
