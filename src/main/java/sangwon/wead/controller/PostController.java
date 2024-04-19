package sangwon.wead.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import sangwon.wead.controller.param.PostFormParam;
import sangwon.wead.resolover.annotation.Referer;
import sangwon.wead.resolover.annotation.UserId;
import sangwon.wead.service.DTO.ListDto;
import sangwon.wead.service.DTO.PostRowDto;
import sangwon.wead.service.ListService;
import sangwon.wead.service.PostService;
import sangwon.wead.service.CommentService;
import sangwon.wead.service.book.BookService;
import sangwon.wead.service.book.search.BookSearchService;


import static sangwon.wead.util.AlertPageRedirector.redirectAlertPage;




@Controller
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;
    private final CommentService commentService;
    private final BookService bookService;
    private final BookSearchService bookSearchService;
    private final ListService listService;



    @GetMapping( "/posts")
    public String list(@RequestParam(value = "page", required = false, defaultValue = "1") int pageNumber,
                           @RequestParam(value = "query", required = false, defaultValue = "") String query,
                           @RequestParam(value = "search", required = false, defaultValue = "") String search,
                           @Referer String referer,
                           Model model) {

        if(query.length() > 50) return redirectAlertPage("검색어는 최대 50자입니다.", referer, model);

        final String finalQuery = query;
        ListService.PageFactory<PostRowDto> pageFactory = postService::getPostALL;
        switch (search) {
            case "title" -> pageFactory = pageable -> postService.getPostByTitle(pageable, finalQuery);
            case "book-title" -> pageFactory = pageable -> postService.getPostByBookTitle(pageable, finalQuery);
            case "nickname" -> pageFactory = pageable -> postService.getPostByNickname(pageable, finalQuery);
            default -> {
                query = "";
                search = "";
            }
        }

        ListDto<PostRowDto> listDto = listService.builder(pageFactory)
                .setUrl("/posts")
                .setQuery("search", search)
                .setQuery("query", query)
                .build(PageRequest.of(pageNumber, 10, Sort.by("postId").descending()));

        model.addAttribute("list", listDto.getList());
        model.addAttribute("pageBar",listDto.getPageBar());
        return "page/post_list";
    }

    /*

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

     */

}


