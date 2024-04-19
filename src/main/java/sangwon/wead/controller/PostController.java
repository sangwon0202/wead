package sangwon.wead.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import sangwon.wead.controller.param.PostFormParam;
import sangwon.wead.resolover.annotation.Referer;
import sangwon.wead.resolover.annotation.UserId;
import sangwon.wead.service.PostService;
import sangwon.wead.service.CommentService;
import sangwon.wead.service.book.BookService;


import static sangwon.wead.util.AlertPageRedirector.redirectAlertPage;

/*


@Controller
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;
    private final CommentService commentService;
    private final BookService bookService;
    private final BookSearchService bookSearchService;

    private final ModelBuilder modelBuilder;


    @GetMapping( "/posts")
    public String list(@RequestParam(value = "page", required = false, defaultValue = "1") int pageNumber,
                           @RequestParam(value = "query", required = false) String query,
                           @RequestParam(value = "search", required = false) String search,
                           @Referer String referer,
                           Model model) {

        PageAdapter pageAdapter = null;
        if(search != null) {
            if(search.equals("nickname")) pageAdapter = new PostInfoPageByNicknameAdapter(postService);
            if(search.equals("title")) pageAdapter = new PostInfoPageByTitleAdapter(postService);
            if(search.equals("book")) pageAdapter = new PostInfoPageByBookTitleAdapter(postService);
        }

        if(pageAdapter != null) {
            if(query == null || query.isBlank()) return redirectAlertPage("검색어를 입력해주세요.", referer, model);
            if(query.length() > 50) return redirectAlertPage("검색어는 최대 50자입니다.", referer, model);
        }

        if(pageAdapter == null) {
            search = null;
            query = null;
            pageAdapter = new PostInfoPageAdapter(postService);
        }

        Page<PostInfo> page = PageFactory.builder()
                .pageAdapter(pageAdapter)
                .pageNumber(pageNumber)
                .pageSize(10)
                .sort(Sort.by("id").descending())
                .query(query)
                .build().getPage(PostInfo.class);

        model.addAttribute("sectionModel",modelBuilder.buildPostListModel(page, search, query));
        return "page/post_list";
    }


    @GetMapping("/posts/{postId}")
    public String read(@UserId(required = false) String userId,
                       @PathVariable("postId") Long postId,
                       Model model) {
        postService.increasePostViews(postId);

        PostInfo postInfo = postService.getPostInfo(postId);
        BookInfo bookInfo = bookService.getBookInfo(postInfo.getIsbn());
        List<CommentInfo> commentInfoList = commentService.getCommentInfoList(postId);

        model.addAttribute("sectionModel", modelBuilder.buildPostReadModel(postInfo, bookInfo, commentInfoList, userId));
        return "page/post_read";
    }

    @GetMapping("/books/{isbn}/posts/upload")
    public String uploadForm(@PathVariable("isbn") String isbn, Model model) {
        BookInfo bookInfo = bookSearchService.getBookInfo(isbn);
        PostFormParam postFormParam = PostFormParam.builder().build();
        model.addAttribute("sectionModel", modelBuilder.buildPostFormModel(bookInfo, postFormParam, false));
        return "page/post_form";
    }

    @PostMapping("/books/{isbn}/posts/upload")
    public String upload(@PathVariable("isbn") String isbn,
                         @Valid @ModelAttribute PostFormParam postFormParam,
                         BindingResult bindingResult,
                         @UserId String userId,
                         Model model,
                         RedirectAttributes redirectAttributes) {

        if(bindingResult.hasErrors()) {
            BookInfo bookInfo = bookSearchService.getBookInfo(isbn);
            model.addAttribute("sectionModel", modelBuilder.buildPostFormModel(bookInfo, postFormParam, true));
            return "page/post_form";
        }

        if(!bookService.checkBookExistence(isbn)) {
            BookInfo bookInfo = bookSearchService.getBookInfo(isbn);
            bookService.saveBook(bookInfo);
        }
        Long postId = postService.uploadPost(postFormParam.toPostUploadForm(userId, isbn));

        redirectAttributes.addAttribute("postId", postId);
        return "redirect:/posts/{postId}";
    }

    @GetMapping("/posts/{postId}/update")
    public String updateForm(@PathVariable("postId") Long postId,
                             Model model) {
        PostInfo postInfo = postService.getPostInfo(postId);
        BookInfo bookInfo = bookService.getBookInfo(postInfo.getIsbn());
        PostFormParam postFormParam = PostFormParam.builder()
                .title(postInfo.getTitle())
                .content(postInfo.getContent())
                .build();

        model.addAttribute("sectionModel", modelBuilder.buildPostFormModel(bookInfo, postFormParam, false, postId));
        return "page/post_form";
    }

    @PostMapping("/posts/{postId}/update")
    public String update(@PathVariable("postId") Long postId,
                         @Valid @ModelAttribute PostFormParam postFormParam,
                         BindingResult bindingResult,
                         Model model,
                         RedirectAttributes redirectAttributes) {

        if(bindingResult.hasErrors()) {
            PostInfo postInfo = postService.getPostInfo(postId);
            BookInfo bookInfo = bookService.getBookInfo(postInfo.getIsbn());
            model.addAttribute("sectionModel", modelBuilder.buildPostFormModel(bookInfo, postFormParam, true, postId));
            return "page/post_form";
        }

        postService.updatePost(postFormParam.toPostUpdateForm(postId));
        redirectAttributes.addAttribute("postId", postId);
        return "redirect:/posts/{postId}";
    }

    @PostMapping("/posts/{postId}/delete")
    public String delete(@PathVariable("postId") Long postId) {
        postService.deletePost(postId);
        return "redirect:/posts";
    }


}


 */