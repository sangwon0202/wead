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
import sangwon.wead.controller.DTO.*;
import sangwon.wead.service.DTO.*;
import sangwon.wead.service.PostService;
import sangwon.wead.service.CommentService;
import sangwon.wead.service.book.BookService;
import sangwon.wead.service.book.search.BookSearchService;
import sangwon.wead.util.AlertPageRedirector;

import java.util.List;

import static sangwon.wead.util.AlertPageRedirector.redirectAlertPage;


@Controller
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;
    private final CommentService commentService;
    private final BookService bookService;
    private final BookSearchService bookSearchService;
    private final DTOConverter dtoConverter;


    @GetMapping( "/posts")
    public String postList(@RequestParam(value = "page", required = false, defaultValue = "1") int pageNumber,
                       Model model) {
        // 게시글 리스트
        Page<PostInfo> page = PageAdjuster
                .pageAdapter(new PostInfoPageAdapter(postService))
                .getPage(pageNumber, 10, PostInfo.class);

        // DTO 전환
        List<PostLine> postList = page.getContent()
                .stream()
                .map(dtoConverter::postInfoToPostLine)
                .toList();

        model.addAttribute("postList", postList);
        model.addAttribute("pageBar", new PageBar(page, 10));
        return "page/post-list";
    }


    @GetMapping("/posts/{postId}")
    public String post(@UserId(required = false) String userId,
                       @PathVariable("postId") Long postId,
                       Model model) {
        // 조회수 올리기
        postService.increasePostViews(postId);

        // 게시글, 댓글, 책 정보
        PostInfo postInfo = postService.getPostInfo(postId);
        List<CommentInfo> commentInfoList = commentService.getCommentInfoList(postId);
        BookInfo bookInfo = bookService.getBookInfo(postInfo.getIsbn());

        //DTO 전환
        PostView post = dtoConverter.postInfoToPostView(postInfo, userId);
        List<CommentView> commentList =commentInfoList
                .stream()
                .map(commentInfo -> dtoConverter.commentInfoToCommentView(commentInfo, userId))
                .toList();

        model.addAttribute("post", post);
        model.addAttribute("commentList", commentList);
        model.addAttribute("book", new BookView(bookInfo));
        return "page/post";
    }

    @GetMapping("/books/{isbn}/posts/upload")
    public String uploadForm(@PathVariable("isbn") String isbn, Model model) {
        // 업로드 폼
        BookInfo bookInfo = bookSearchService.getBookInfo(isbn);
        model.addAttribute("bookTitle", bookInfo.getTitle());
        model.addAttribute("isbn", bookInfo.getIsbn());

        return "page/post-upload";
    }

    @PostMapping("/books/{isbn}/posts/upload")
    public String upload(@PathVariable("isbn") String isbn,
                         @Valid @ModelAttribute PostUploadParam postUploadParam,
                         BindingResult bindingResult,
                         @UserId String userId,
                         @Referer String referer,
                         Model model,
                         RedirectAttributes redirectAttributes) {
        // 제목 및 내용이 입력되지 않을 경우
        if(bindingResult.hasErrors()) {
            return redirectAlertPage("제목 및 내용을 모두 입력해주세요.", referer, model);
        }

        // 책 등록 및 게시글 업로드
        if(!bookService.checkBookExistence(isbn)) {
            BookInfo bookInfo = bookSearchService.getBookInfo(isbn);
            bookService.saveBook(bookInfo);
        }
        Long postId = postService.uploadPost(postUploadParam.toPostUploadForm(userId, isbn));


        redirectAttributes.addAttribute("postId", postId);
        return "redirect:/posts/{postId}";
    }

    @GetMapping("/posts/{postId}/update")
    public String updateForm(@PathVariable("postId") Long postId,
                             Model model) {
        // 수정 폼 전송
        PostInfo postInfo = postService.getPostInfo(postId);
        BookInfo bookInfo = bookSearchService.getBookInfo(postInfo.getIsbn());

        model.addAttribute("title", postInfo.getTitle());
        model.addAttribute("content", postInfo.getContent());
        model.addAttribute("bookTitle", bookInfo.getTitle());
        return "page/post-update";
    }

    @PostMapping("/posts/{postId}/update")
    public String update(@PathVariable("postId") Long postId,
                         @Valid @ModelAttribute PostUpdateParam postUpdatePram,
                         BindingResult bindingResult,
                         @Referer String referer,
                         Model model,
                         RedirectAttributes redirectAttributes) {
        // 제목 및 내용이 입력되지 않을 경우
        if(bindingResult.hasErrors()) {
            return AlertPageRedirector.redirectAlertPage("제목과 내용을 모두 입력해주세요.", referer, model);
        }
        // 게시글 수정
        PostInfo postInfo = postService.getPostInfo(postId);
        postService.updatePost(postUpdatePram.toPostUpdateForm(postInfo.getPostId()));
        redirectAttributes.addAttribute("postId", postId);
        return "redirect:/posts/{postId}";
    }

    @PostMapping("/posts/{postId}/delete")
    public String delete(@PathVariable("postId") Long postId) {
        // 게시글 삭제
        postService.deletePost(postId);
        return "redirect:/posts";
    }


}
