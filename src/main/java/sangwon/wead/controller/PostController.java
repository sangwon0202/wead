package sangwon.wead.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import sangwon.wead.controller.DTO.PostFormParam;
import sangwon.wead.resolover.annotation.Referer;
import sangwon.wead.resolover.annotation.UserId;
import sangwon.wead.service.DTO.*;
import sangwon.wead.service.ListService;
import sangwon.wead.service.PostService;
import sangwon.wead.service.CommentService;
import sangwon.wead.service.UserService;
import sangwon.wead.service.book.BookService;
import sangwon.wead.service.book.search.BookSearchService;


import static sangwon.wead.util.AlertPageRedirector.redirectAlertPage;


@Controller
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;
    private final CommentService commentService;
    private final BookService bookService;
    private final UserService userService;
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

    @GetMapping("/posts/{postId}")
    public String read(@PathVariable("postId") Long postId,
                       @RequestParam(value = "page", required = false, defaultValue = "1") int pageNumber,
                       Model model) {
        postService.increasePostViews(postId);
        PostDto postDto = postService.getPost(postId);
        UserDto userDto = userService.getUser(postDto.getUserId());
        BookDto bookDto = bookService.getBook(postDto.getIsbn());

        ListDto<CommentRowDto> listDto = listService.builder(pageable -> commentService.getCommentByPostId(pageable, postId))
                .setUrl("/posts/" + postId)
                .build(PageRequest.of(pageNumber, 10, Sort.by("commentId")));

        model.addAttribute("post", postDto);
        model.addAttribute("user", userDto);
        model.addAttribute("book", bookDto);
        model.addAttribute("comment", listDto);
        return "page/post_read";
    }

    @GetMapping("/books/{isbn}/posts/upload")
    public String uploadForm(@PathVariable("isbn") String isbn, Model model) {
        BookDto bookDto = bookSearchService.getBook(isbn);
        model.addAttribute("book", bookDto);
        model.addAttribute("type", "upload");
        return "page/post_form";
    }


    @PostMapping("/books/{isbn}/posts/upload")
    public String upload(@PathVariable("isbn") String isbn,
                         @Valid @ModelAttribute PostFormParam postFormParam,
                         BindingResult bindingResult,
                         @UserId String userId,
                         Model model) {
        BookDto bookDto = bookSearchService.getBook(isbn);

        if(bindingResult.hasErrors()) {
            model.addAttribute("book", bookDto);
            model.addAttribute("type", "upload");
            model.addAttribute("title", postFormParam.getTitle());
            model.addAttribute("content", postFormParam.getContent());
            model.addAttribute("alert", true);
            return "page/post_form";
        }

        bookService.saveBook(bookDto);
        Long postId = postService.uploadPost(postFormParam.toPostUploadDto(userId, isbn));
        return "redirect:/posts/" + postId;
    }

    @GetMapping("/posts/{postId}/update")
    public String updateForm(@PathVariable("postId") Long postId,
                             @UserId String userId,
                             Model model) {
        postService.checkPostPermission(postId, userId);
        PostDto postDto = postService.getPost(postId);
        BookDto bookDto = bookService.getBook(postDto.getIsbn());

        model.addAttribute("book", bookDto);
        model.addAttribute("type", "update");
        model.addAttribute("title", postDto.getTitle());
        model.addAttribute("content", postDto.getContent());
        return "page/post_form";
    }

    @PostMapping("/posts/{postId}/update")
    public String update(@PathVariable("postId") Long postId,
                         @Valid @ModelAttribute PostFormParam postFormParam,
                         BindingResult bindingResult,
                         @UserId String userId,
                         Model model) {
        postService.checkPostPermission(postId, userId);
        PostDto postDto = postService.getPost(postId);
        BookDto bookDto = bookService.getBook(postDto.getIsbn());

        if(bindingResult.hasErrors()) {
            model.addAttribute("book", bookDto);
            model.addAttribute("type", "update");
            model.addAttribute("title", postFormParam.getTitle());
            model.addAttribute("content", postFormParam.getContent());
            model.addAttribute("alert", true);
            return "page/post_form";
        }

        postService.updatePost(postFormParam.toPostUpdateDto(postId));
        return "redirect:/posts/" + postId;
    }

    @PostMapping("/posts/{postId}/delete")
    public String delete(@PathVariable("postId") Long postId,
                         @UserId String userId) {
        postService.checkPostPermission(postId, userId);
        postService.deletePost(postId);
        return "redirect:/posts";
    }
}


