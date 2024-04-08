package sangwon.wead.controller;


import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import sangwon.wead.exception.NotLoginException;
import sangwon.wead.exception.PermissionException;
import sangwon.wead.service.DTO.CommentInfo;
import sangwon.wead.service.DTO.CommentUploadForm;
import sangwon.wead.service.CommentService;

import static sangwon.wead.controller.util.AlertPageRedirector.redirectAlertPage;

@Controller
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;


    @PostMapping("/comment/upload/{postId}")
    public String upload(HttpServletRequest request,
                         @PathVariable("postId") Long postId,
                         @RequestParam("content") String content,
                         Model model) {

        // 로그인 정보
        HttpSession session = request.getSession();
        String userId = (String)session.getAttribute("userId");

        // 이전 URL
        String referer = request.getHeader("referer");

        // 로그인이 안되어 있을 경우
        if(userId == null) return redirectAlertPage("로그인을 먼저 해주세요.", referer, model);

        // 댓글이 입력되지 않은 경우
        if(content.isBlank()) return redirectAlertPage("내용을 작성해주세요", referer, model);

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
                         @PathVariable("commentId") Long commentId,
                         Model model) {

        // 로그인 정보
        HttpSession session = request.getSession();
        String userId = (String)session.getAttribute("userId");

        // 이전 URL
        String referer = request.getHeader("referer");

        // 로그인이 안되어 있을 경우
        if(userId == null) throw new NotLoginException();

        // 수정 권한이 없는 경우
        CommentInfo commentInfo = commentService.getCommentInfo(commentId);
        if(!commentInfo.getUserId().equals(userId)) throw new PermissionException();

        // 댓글 삭제
        commentService.deleteComment(commentId);
        return "redirect:" + referer;
    }

}
