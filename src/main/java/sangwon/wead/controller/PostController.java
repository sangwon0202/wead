package sangwon.wead.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import sangwon.wead.page.PageAdjuster;
import sangwon.wead.page.PostInfoPageAdapter;
import sangwon.wead.resolover.annotation.Referer;
import sangwon.wead.resolover.annotation.UserId;
import sangwon.wead.aspect.annotation.CheckLogin;
import sangwon.wead.aspect.annotation.NeedLogin;
import sangwon.wead.controller.DTO.*;
import sangwon.wead.exception.ClientFaultException;
import sangwon.wead.exception.NonexistentPostException;
import sangwon.wead.service.DTO.*;
import sangwon.wead.service.PostService;
import sangwon.wead.service.CommentService;
import sangwon.wead.service.book.BookService;
import sangwon.wead.util.AlertPageRedirector;

import java.util.List;

import static sangwon.wead.util.AlertPageRedirector.redirectAlertPage;


@Controller
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;
    private final CommentService commentService;
    private final BookService bookService;


    @GetMapping( "/post")
    public String postList(@RequestParam(value = "page", required = false, defaultValue = "1") int pageNumber,
                       Model model) {
        // 게시글 리스트
        Page<PostInfo> page = PageAdjuster
                .pageAdapter(new PostInfoPageAdapter(postService))
                .getPage(pageNumber, 10, PostInfo.class);

        model.addAttribute("postList", page.getContent().stream().map(postInfo -> new PostLine(postInfo)));
        model.addAttribute("pageBar", new PageBar(page, 10));
        return "page/post-list";
    }


    @GetMapping("/post/{postId}")
    public String post(@UserId(required = false) String userId,
                       @PathVariable("postId") Long postId,
                       Model model) {
        // 게시글이 존재하지 않을 경우
        if(!postService.checkPostExistence(postId)) throw new NonexistentPostException();

        // 조회수 올리기
        postService.increasePostViews(postId);
        // 게시글, 댓글, 책 정보
        PostInfo postInfo = postService.getPostInfo(postId);
        List<CommentInfo> commentInfoList = commentService.getCommentInfoList(postId);
        BookInfo bookInfo = bookService.getBookInfo(postInfo.getIsbn());

        model.addAttribute("post", new PostView(postInfo, userId));
        model.addAttribute("commentList",commentInfoList.stream().map(commentInfo -> new CommentView(commentInfo, userId)).toList());
        model.addAttribute("book", new BookView(bookInfo));

        return "page/post";
    }

    @NeedLogin
    @GetMapping("/post/upload")
    public String uploadForm(@RequestParam("isbn") String isbn, Model model) {
        // 수정 폼 전송
        BookInfo bookInfo = bookService.getBookInfo(isbn);
        model.addAttribute("bookTitle", bookInfo.getTitle());
        model.addAttribute("isbn", bookInfo.getIsbn());
        return "page/post-upload";
    }

    @CheckLogin
    @PostMapping("/post/upload")
    public String upload(@Valid @ModelAttribute PostUploadParam postUploadParam,
                         BindingResult bindingResult,
                         @UserId String userId,
                         @Referer String referer,
                         Model model,
                         RedirectAttributes redirectAttributes) {
        // 제목 및 내용이 입력되지 않을 경우
        if(bindingResult.hasErrors()) return redirectAlertPage("제목 및 내용을 모두 입력해주세요.", referer, model);

        // 게시글 업로드
        Long postId = postService.uploadPost(postUploadParam.toPostUploadForm(userId));
        redirectAttributes.addAttribute("postId", postId);
        return "redirect:/post/{postId}";
    }

    @CheckLogin
    @GetMapping("/post/update/{postId}")
    public String updateForm(@PathVariable("postId") Long postId,
                             @UserId String userId,
                             Model model) {
        // 게시글이 존재하지 않을 경우
        if(!postService.checkPostExistence(postId)) throw new ClientFaultException();
        // 수정 권한이 없을 경우
        if(!postService.getPostInfo(postId).getUserId().equals(userId)) throw new ClientFaultException();

        // 수정 폼 전송
        PostInfo postInfo = postService.getPostInfo(postId);
        BookInfo bookInfo = bookService.getBookInfo(postInfo.getIsbn());

        model.addAttribute("title", postInfo.getTitle());
        model.addAttribute("content", postInfo.getContent());
        model.addAttribute("bookTitle", bookInfo.getTitle());
        return "page/post-update";
    }


    @CheckLogin
    @PostMapping("/post/update/{postId}")
    public String update(@PathVariable("postId") Long postId,
                         @Valid @ModelAttribute PostUpdateParam postUpdatePram,
                         BindingResult bindingResult,
                         @UserId String userId,
                         @Referer String referer,
                         Model model,
                         RedirectAttributes redirectAttributes) {
        // 게시글이 존재하지 않을 경우
        if(!postService.checkPostExistence(postId)) throw new ClientFaultException();
        // 수정 권한이 없을 경우
        if(!postService.getPostInfo(postId).getUserId().equals(userId)) throw new ClientFaultException();
        // 제목 및 내용이 입력되지 않을 경우
        if(bindingResult.hasErrors()) {
            return AlertPageRedirector.redirectAlertPage("제목과 내용을 모두 입력해주세요.", referer, model);
        }

        // 게시글 수정
        PostInfo postInfo = postService.getPostInfo(postId);
        postService.updatePost(postUpdatePram.toPostUpdateForm(postInfo.getPostId()));
        redirectAttributes.addAttribute("postId", postId);
        return "redirect:/post/{postId}";
    }

    @CheckLogin
    @PostMapping("/post/delete/{postId}")
    public String delete(@PathVariable("postId") Long postId,
                         @UserId String userId) {
        // 게시글이 존재하지 않을 경우
        if(!postService.checkPostExistence(postId)) throw new ClientFaultException();
        // 삭제 권한이 없을 경우
        if(!postService.getPostInfo(postId).getUserId().equals(userId)) throw new ClientFaultException();

        // 게시글 삭제
        postService.deletePost(postId);
        return "redirect:/post";
    }


}
