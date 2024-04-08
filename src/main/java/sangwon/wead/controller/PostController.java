package sangwon.wead.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import sangwon.wead.controller.DTO.*;
import sangwon.wead.exception.NotLoginException;
import sangwon.wead.exception.PermissionException;
import sangwon.wead.service.DTO.*;
import sangwon.wead.service.PostService;
import sangwon.wead.service.CommentService;
import sangwon.wead.service.UserService;
import sangwon.wead.service.book.BookService;

import java.util.List;

import static sangwon.wead.controller.util.AlertPageRedirector.redirectAlertPage;


@Controller
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;
    private final CommentService commentService;
    private final BookService bookService;
    private final UserService userService;


    @GetMapping({"/", "/post"})
    public String postList(@RequestParam(value = "page", required = false, defaultValue = "1") int pageNumber,
                       HttpServletRequest request,
                       Model model) {

        // 로그인 정보
        HttpSession session = request.getSession();
        String userId = (String)session.getAttribute("userId");
        if(userId != null)  model.addAttribute("nickname", userService.getUserInfo(userId).getNickname());

        if(pageNumber < 1) pageNumber = 1;
        Page<PostInfo> page = postService.getPostInfoPage(pageNumber-1, 10);
        int totalPages = page.getTotalPages();
        if(totalPages == 0) totalPages = 1;
        if(pageNumber > totalPages) page = postService.getPostInfoPage(totalPages-1, 10);

        model.addAttribute("postList", page.getContent().stream().map(postInfo -> new PostLine(postInfo)));
        model.addAttribute("pageBar", new PageBar(page, 10));
        return "page/post-list";
    }


    @GetMapping("/post/{postId}")
    public String post(HttpServletRequest request,
                       @PathVariable("postId") Long postId,
                       Model model) {

        // 로그인 정보
        HttpSession session = request.getSession();
        String userId = (String)session.getAttribute("userId");
        if(userId != null)  model.addAttribute("nickname", userService.getUserInfo(userId).getNickname());

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




    @GetMapping("/post/upload")
    public String uploadForm(@RequestParam("isbn") String isbn, HttpServletRequest request, Model model) {

        // 로그인 정보
        HttpSession session = request.getSession();
        String userId = (String)session.getAttribute("userId");
        if(userId != null)  model.addAttribute("nickname", userService.getUserInfo(userId).getNickname());

        // 이전 URL
        String referer = request.getHeader("referer");

        // 로그인이 안되어 있을 경우
        if(userId == null) return redirectAlertPage("로그인을 먼저 해주세요.", referer, model);

        BookInfo bookInfo = bookService.getBookInfo(isbn);
        model.addAttribute("bookTitle", bookInfo.getTitle());
        model.addAttribute("isbn", bookInfo.getIsbn());
        return "page/post-upload";
    }




    @PostMapping("/post/upload")
    public String upload(HttpServletRequest request,
                         @Valid @ModelAttribute PostUploadParam postUploadParam,
                         BindingResult bindingResult,
                         Model model,
                         RedirectAttributes redirectAttributes) {

        // 로그인 정보
        HttpSession session = request.getSession();
        String userId = (String)session.getAttribute("userId");

        // 로그인이 안되어 있을 경우
        if(userId == null) throw new NotLoginException();

        // 이전 URL
        String referer = request.getHeader("referer");

        // 제목 및 내용이 입력되지 않을 경우
        if(bindingResult.hasErrors()) return redirectAlertPage("제목 및 내용을 모두 입력해주세요.", referer, model);

        Long postId = postService.uploadPost(postUploadParam.toPostUploadForm(userId));
        redirectAttributes.addAttribute("postId", postId);
        return "redirect:/post/{postId}";
    }


    @GetMapping("/post/update/{postId}")
    public String updateForm(HttpServletRequest request,
                             @PathVariable("postId") Long postId,
                             Model model) {

        // 로그인 정보
        HttpSession session = request.getSession();
        String userId = (String)session.getAttribute("userId");
        if(userId != null)  model.addAttribute("nickname", userService.getUserInfo(userId).getNickname());

        // 로그인이 안되어 있을 경우
        if(userId == null) throw new NotLoginException();

        PostInfo postInfo = postService.getPostInfo(postId);

        // 수정 권한이 없는 경우
        if(!postInfo.getUserId().equals(userId)) throw new PermissionException();

        BookInfo bookInfo = bookService.getBookInfo(postInfo.getIsbn());

        model.addAttribute("title", postInfo.getTitle());
        model.addAttribute("content", postInfo.getContent());
        model.addAttribute("bookTitle", bookInfo.getTitle());
        return "page/post-update";
    }


    @PostMapping("/post/update/{postId}")
    public String update(HttpServletRequest request,
                         @PathVariable("postId") Long postId,
                         @Valid @ModelAttribute PostUpdateParam postUpdatePram,
                         BindingResult bindingResult,
                         RedirectAttributes redirectAttributes,
                         Model model) {

        // 로그인 정보
        HttpSession session = request.getSession();
        String userId = (String)session.getAttribute("userId");

        // 로그인이 안되어 있을 경우
        if(userId == null) throw new NotLoginException();

        // 이전 URL
        String referer = request.getHeader("referer");

        // 제목 및 내용이 입력되지 않을 경우
        if(bindingResult.hasErrors()) return redirectAlertPage("제목 및 내용을 모두 입력해주세요.", referer, model);

        PostInfo postInfo = postService.getPostInfo(postId);

        // 수정 권한이 없는 경우
        if(!postInfo.getUserId().equals(userId)) throw new PermissionException();

        postService.updatePost(postUpdatePram.toPostUpdateForm(postInfo.getPostId()));
        redirectAttributes.addAttribute("postId", postId);
        return "redirect:/post/{postId}";
    }

    @PostMapping("/post/delete/{postId}")
    public String delete(HttpServletRequest request,
                             @PathVariable("postId") Long postId,
                             Model model) {

        // 로그인 정보
        HttpSession session = request.getSession();
        String userId = (String)session.getAttribute("userId");

        // 로그인이 안되어 있을 경우
        if(userId == null) throw new NotLoginException();

        PostInfo postInfo = postService.getPostInfo(postId);

        // 수정 권한이 없는 경우
        if(!postInfo.getUserId().equals(userId)) throw new PermissionException();

        postService.deletePost(postId);
        return "redirect:/post";

    }


}
