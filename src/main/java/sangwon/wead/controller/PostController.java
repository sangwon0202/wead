package sangwon.wead.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import sangwon.wead.DTO.*;
import sangwon.wead.service.PostService;
import sangwon.wead.service.CommentService;
import sangwon.wead.service.UserService;
import sangwon.wead.service.book.BookService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


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
        if(userId != null)  model.addAttribute("userInfo", userService.getUserInfo(userId));

        pageNumber -= 1;
        Page<PostInfo> page = postService.getPostInfoPage(pageNumber, 10);
        model.addAttribute("postList", page.getContent());
        model.addAttribute("pageBar", new PageBar(page, 10));
        return "page/post-list";
    }


    @GetMapping("/post/{postId}")
    public String post(HttpServletRequest request,
                       @PathVariable("postId") Long postId,
                       Model model) {

        HttpSession session = request.getSession();
        String userId = (String)session.getAttribute("userId");

        // 게시글, 댓글, 책 정보
        PostInfo postInfo = postService.getPostInfo(postId);
        List<CommentInfo> commentInfoList = commentService.getCommentInfoList(postId);
        BookInfo bookInfo = bookService.getBookInfo(postInfo.getIsbn());

        model.addAttribute("postInfo", postInfo);
        model.addAttribute("commentInfoList", commentInfoList);
        model.addAttribute("bookInfo", bookInfo);

        // 게시글 및 댓글 수정, 삭제 여부
        boolean postPermission = (userId != null) && (postInfo.getUserId().equals(userId));
        Map<Long, Boolean> commentPermission = new HashMap<>();
        commentInfoList.stream().forEach(commentInfo -> {
            boolean Permission = (userId != null) && (commentInfo.getUserId().equals(userId));
            commentPermission.put(commentInfo.getCommentId(), Permission);
        });

        model.addAttribute("postPermission", postPermission);
        model.addAttribute("commentPermission", commentPermission);

        postService.increasePostViews(postId);
        return "page/post";
    }


    @GetMapping("/post/upload/{isbn}")
    public String uploadForm(@PathVariable("isbn") String isbn, HttpServletRequest request, Model model) {

        HttpSession session = request.getSession();
        String userId = (String)session.getAttribute("userId");

        // 로그인이 안되어 있을 경우
        if(userId == null) {
            model.addAttribute("message", "로그인을 먼저 해주세요.");
            return "alert";
        }

        BookInfo bookInfo = bookService.getBookInfo(isbn);
        model.addAttribute("bookInfo", bookInfo);
        model.addAttribute("type", "upload");
        return "page/post-upload";
    }


    @PostMapping("/post/upload")
    public String upload(HttpServletRequest request,
                         @ModelAttribute PostUploadForm postUploadForm,
                         Model model) {

        HttpSession session = request.getSession();
        String userId = (String)session.getAttribute("userId");

        // 로그인이 안되어 있을 경우
        if(userId == null) {
            model.addAttribute("message", "로그인을 먼저 해주세요.");
            return "alert";
        }

        postService.uploadPost(userId, postUploadForm);
        return "redirect:/post";
    }


    @GetMapping("/post/update/{postId}")
    public String updateForm(HttpServletRequest request,
                             @PathVariable("postId") Long postId,
                             Model model) {

        HttpSession session = request.getSession();
        String userId = (String)session.getAttribute("userId");

        // 로그인이 안되어 있을 경우
        if(userId == null) {
            model.addAttribute("message", "로그인을 먼저 해주세요.");
            return "alert";
        }

        PostInfo postInfo = postService.getPostInfo(postId);

        // 수정 권한이 없는 경우
        if(!postInfo.getUserId().equals(userId)) {
            model.addAttribute("message", "권한이 없습니다.");
            return "alert";
        }

        BookInfo bookInfo = bookService.getBookInfo(postInfo.getIsbn());

        model.addAttribute("postInfo", postInfo);
        model.addAttribute("bookInfo", bookInfo);
        model.addAttribute("type", "update");
        return "page/post-upload";
    }



    @PostMapping("/post/update/{postId}")
    public String update(HttpServletRequest request,
                         @PathVariable("postId") Long postId,
                         @ModelAttribute PostUpdateForm postUpdateForm,
                         RedirectAttributes redirectAttributes,
                         Model model) {

        HttpSession session = request.getSession();
        String userId = (String)session.getAttribute("userId");

        // 로그인이 안되어 있을 경우
        if(userId == null) {
            model.addAttribute("message", "로그인을 먼저 해주세요.");
            return "alert";
        }

        PostInfo postInfo = postService.getPostInfo(postId);

        // 수정 권한이 없는 경우
        if(!postInfo.getUserId().equals(userId)) {
            model.addAttribute("message", "권한이 없습니다.");
            return "alert";
        }

        postService.updatePost(postId, postUpdateForm);
        redirectAttributes.addAttribute("postId", postId);
        return "redirect:/post/{postId}";
    }

    @PostMapping("/post/delete/{postId}")
    public String delete(HttpServletRequest request,
                             @PathVariable("postId") Long postId,
                             Model model) {

        HttpSession session = request.getSession();
        String userId = (String)session.getAttribute("userId");

        // 로그인이 안되어 있을 경우
        if(userId == null) {
            model.addAttribute("message", "로그인을 먼저 해주세요.");
            return "alert";
        }

        PostInfo postInfo = postService.getPostInfo(postId);

        // 수정 권한이 없는 경우
        if(!postInfo.getUserId().equals(userId)) {
            model.addAttribute("message", "권한이 없습니다.");
            return "alert";
        }

        postService.deletePost(postId);
        return "redirect:/";

    }

}
