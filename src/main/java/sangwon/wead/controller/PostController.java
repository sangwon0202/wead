package sangwon.wead.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import sangwon.wead.controller.model.*;
import sangwon.wead.exception.*;
import sangwon.wead.service.BookService;
import sangwon.wead.service.DTO.CommentDto;
import sangwon.wead.service.DTO.PostDto;
import sangwon.wead.service.DTO.PostUpdateFormDto;
import sangwon.wead.service.DTO.UserDto;
import sangwon.wead.service.PostService;
import sangwon.wead.service.CommentService;
import sangwon.wead.service.UserService;

import java.util.List;


@Controller
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;
    private final CommentService commentService;
    private final UserService userService;
    private final BookService bookService;
    private final int countPerPage = 10;
    private final int pageCountPerPageBar = 10;

    @GetMapping("/post")
    public String postList(@RequestParam(value = "page", required = false, defaultValue = "1") int pageNumber,
                       HttpServletRequest request,
                       Model model) {

        // 로그인 정보
        HttpSession session = request.getSession();
        String userId = (String)session.getAttribute("userId");

        try { if(userId != null) model.addAttribute("userInfo", new UserInfoModel(userService.getUser(userId))); }
        // 존재하지 않는 회원인 경우 (회원 탈퇴했으나 세션이 동기화 안된 경우)
        catch (NonexistentUserException e) {
            session.removeAttribute("userId");
            return "redirect:/";
        }

        // 게시글 리스트 및 페이지바
        try {
            model.addAttribute("postList", buildPostList(postService.getPostList(pageNumber, countPerPage)));
            model.addAttribute("pageBar",new PageBarModel(postService.getPageBar(pageNumber, countPerPage, pageCountPerPageBar)));
            return "page/post-list";
        }
        // 해당 페이지가 존재하지 않을 경우
        catch (NonexistentPageException e) {
            model.addAttribute("message", "존재하지 않는 페이지입니다.");
            return "alert";
        }
    }

    @GetMapping("/post/{postId}")
    public String read(HttpServletRequest request,
                       @PathVariable("postId") int postId,
                       Model model) {

        HttpSession session = request.getSession();
        String userId = (String)session.getAttribute("userId");

        try {
            model.addAttribute("post", buildPost(userId, postService.read(postId)));
            model.addAttribute("comment", buildComment(userId, commentService.getCommentList(postId)));
            return "page/board";
        }
        catch (NonexistentPostException e) {
            model.addAttribute("message", "존재하지 않는 게시물입니다.");
            return "alert";
        }
    }

    @GetMapping("/post/upload")
    public String uploadForm(@RequestParam("isbn") String isbn, HttpServletRequest request, Model model) {

        HttpSession session = request.getSession();
        String userId = (String)session.getAttribute("userId");

        // 로그인이 안되어 있을 경우
        if(userId == null) {
            model.addAttribute("message", "로그인을 먼저 해주세요.");
            return "alert";
        }

        try {
            model.addAttribute("bookInfo",new BookInfoModel(bookService.getBook(isbn)));
            model.addAttribute("type", "upload");
            return "page/post-upload";
        }
        // API 접속에 장애가 발생할 경우
        catch (APICallFailException e) {
            throw new RuntimeException(e);
        }
        // isbn 에 해당하는 책이 존재하지 않을 경우
        catch (NonexistentBookException e) {
            model.addAttribute("message", "존재하지 않는 책입니다.");
            return "alert";
        }
    }

    @PostMapping("/post/upload")
    public String upload(HttpServletRequest request,
                         @RequestParam("title") String title,
                         @RequestParam("content") String content,
                         Model model) {

        HttpSession session = request.getSession();
        String userId = (String)session.getAttribute("userId");

        // 로그인이 안되어 있을 경우
        if(userId == null) {
            model.addAttribute("message", "로그인을 먼저 해주세요.");
            return "alert";
        }

        // 제목을 입력하지 않은 경우
        if(title.isEmpty()) {
            model.addAttribute("message", "제목을 입력해주세요.");
            return "alert";
        }

        // 내용을 입력하지 않은 경우
        if(content.isEmpty()) {
            model.addAttribute("message", "내용을 입력해주세요.");
            return "alert";
        }

        postService.create(userId, title, content);
        return "redirect:/";
    }

    @GetMapping("/post/update/{postId}")
    public String updateForm(HttpServletRequest request,
                             @PathVariable("postId") int postId,
                             Model model) {

        HttpSession session = request.getSession();
        String userId = (String)session.getAttribute("userId");

        // 로그인이 안되어 있을 경우
        if(userId == null) {
            model.addAttribute("message", "로그인을 먼저 해주세요.");
            return "alert";
        }

        try {
            PostUpdateFormDto postUpdateFormDto = postService.updateForm(userId, postId);
            model.addAttribute("type", "update");
            model.addAttribute("title", postUpdateFormDto.getTitle());
            model.addAttribute("content", postUpdateFormDto.getContent());
            return "page/post-upload";
        }
        // 게시글이 존재하지 않는 경우
        catch (NonexistentPostException e) {
            model.addAttribute("message", "존재하지 않는 게시물입니다.");
            return "alert";
        }
        // 수정 권한이 없는 경우
        catch (PermissionException e) {
            model.addAttribute("message", "권한이 없습니다.");
            model.addAttribute("redirect", "/post/" + postId);
            return "alert";
        }
    }

    @PostMapping("/post/update/{postId}")
    public String update(HttpServletRequest request,
                         @PathVariable("postId") int postId,
                         @RequestParam("title") String title,
                         @RequestParam("content") String content,
                         RedirectAttributes redirectAttributes,
                         Model model) {

        HttpSession session = request.getSession();
        String userId = (String)session.getAttribute("userId");

        // 로그인이 안되어 있을 경우
        if(userId == null) {
            model.addAttribute("message", "로그인을 먼저 해주세요.");
            return "alert";
        }

        // 제목을 입력하지 않은 경우
        if(title.isEmpty()) {
            model.addAttribute("message", "제목을 입력해주세요.");
            return "alert";
        }

        // 내용을 입력하지 않은 경우
        if(content.isEmpty()) {
            model.addAttribute("message", "내용을 입력해주세요.");
            return "alert";
        }

        try {
            postService.update(userId, postId, title, content);
            redirectAttributes.addAttribute("postId", postId);
            return "redirect:/post/{postId}";
        }
        // 게시글이 존재하지 않는 경우
        catch (NonexistentPostException e) {
            model.addAttribute("message", "존재하지 않는 게시물입니다.");
            return "alert";
        }
        // 수정 권한이 없는 경우
        catch (PermissionException e) {
            model.addAttribute("message", "권한이 없습니다.");
            model.addAttribute("redirect", "/post/" + postId);
            return "alert";
        }
    }

    @PostMapping("/post/delete/{postId}")
    public String delete(HttpServletRequest request,
                             @PathVariable("postId") int postId,
                             Model model) {

        HttpSession session = request.getSession();
        String userId = (String)session.getAttribute("userId");

        // 로그인이 안되어 있을 경우
        if(userId == null) {
            model.addAttribute("message", "로그인을 먼저 해주세요.");
            return "alert";
        }

        try {
            postService.delete(userId, postId);
            return "redirect:/";
        }
        // 게시글이 존재하지 않는 경우
        catch (NonexistentPostException e) {
            model.addAttribute("message", "존재하지 않는 게시물입니다.");
            return "alert";
        }
        // 삭제 권한이 없는 경우
        catch (PermissionException e) {
            model.addAttribute("message", "권한이 없습니다.");
            model.addAttribute("redirect", "/post/" + postId);
            return "alert";
        }
    }

    private List<PostLineModel> buildPostList(List<PostDto> postDtoList) {
        return postDtoList.stream().map((postDto -> {
            String nickname;
            try {
                UserDto userDto = userService.getUser(postDto.getUserId());
                nickname = userDto.getNickname();
            }
            catch (NonexistentUserException e) { nickname = "알 수 없음"; }

            int commentCount;
            try {
                commentCount = commentService.getCommentList(postDto.getPostId()).size();
            }
            catch (NonexistentPostException e) { commentCount = 0; }

            return PostLineModel.builder()
                    .postId(postDto.getPostId())
                    .title(postDto.getTitle())
                    .commentCount(commentCount)
                    .nickname(nickname)
                    .uploadDate(postDto.getUploadDate())
                    .views(postDto.getViews())
                    .build();
        })).toList();
    }

    private PostModel buildPost(String userId, PostDto postDto) {
        String nickname;
        try {UserDto userDto = userService.getUser(postDto.getUserId());
            nickname = userDto.getNickname();
        }
        catch (NonexistentUserException e) { nickname = "알 수 없음"; }

        return PostModel.builder()
                .postId(postDto.getPostId())
                .title(postDto.getTitle())
                .nickname(nickname)
                .uploadDate(postDto.getUploadDate())
                .views(postDto.getViews())
                .permission(userId != null && postDto.getUserId().equals(userId))
                .content(postDto.getContent())
                .build();
    }

    private List<CommentModel> buildComment(String userId, List<CommentDto> commentDtoList) {
        return commentDtoList.stream().map((commentDto -> {
            String nickname;
            try {UserDto userDto = userService.getUser(commentDto.getUserId());
                nickname = userDto.getNickname();
            }
            catch (NonexistentUserException e) { nickname = "알 수 없음"; }

            return CommentModel.builder()
                    .commentId(commentDto.getCommentId())
                    .nickname(nickname)
                    .content(commentDto.getContent())
                    .uploadDate(commentDto.getUploadDate())
                    .permission(userId != null && commentDto.getUserId().equals(userId))
                    .build();
        })).toList();
    }
}
