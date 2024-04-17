package sangwon.wead.controller.model.builder;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;
import sangwon.wead.controller.model.*;
import sangwon.wead.controller.param.PostFormParam;
import sangwon.wead.service.CommentService;
import sangwon.wead.service.DTO.BookInfo;
import sangwon.wead.service.DTO.CommentInfo;
import sangwon.wead.service.DTO.PostInfo;
import sangwon.wead.service.DTO.UserInfo;
import sangwon.wead.service.UserService;
import sangwon.wead.service.book.BookService;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class ModelBuilder {

    private final UserService userService;
    private final CommentService commentService;
    private final BookService bookService;

    public PostListModel buildPostListModel(Page<PostInfo> page, String search, String query) {
        List<PostListModel.Post> postList = page.getContent().stream()
                .map(postInfo ->
                    PostListModel.Post.builder()
                            .postId(postInfo.getPostId())
                            .nickname(userService.getUserInfo(postInfo.getUserId()).getNickname())
                            .title(postInfo.getTitle())
                            .uploadDate(formatLocalDate(postInfo.getUploadDate()))
                            .views(postInfo.getViews())
                            .commentCount(commentService.getCommentInfoList(postInfo.getPostId()).size())
                            .image(bookService.getBookInfo(postInfo.getIsbn()).getImage())
                            .build()
                )
                .toList();

        Map<String, String> queryMap = new HashMap<>();
        if(search != null) queryMap.put("search", search);
        if(query != null) queryMap.put("query", query);
        PageBarModel pageBarModel = buildPageBarModel(page, 10, "/posts", queryMap);

        Map<String, String> optionMap = new HashMap<>();
        optionMap.put("title", "게시글 제목");
        optionMap.put("book", "책 제목");
        optionMap.put("nickname", "작성자 이름");
        SearchBarModel searchBarModel = SearchBarModel.builder()
                .url("/posts")
                .optionMap(optionMap)
                .build();

        return PostListModel.builder()
                .postList(postList)
                .pageBarModel(pageBarModel)
                .searchBarModel(searchBarModel)
                .build();
    }

    public PostReadModel buildPostReadModel(PostInfo postInfo, BookInfo bookInfo, List<CommentInfo> commentInfoList, String userId) {
        PostReadModel.Post post = PostReadModel.Post.builder()
                .postId(postInfo.getPostId())
                .nickname(userService.getUserInfo(postInfo.getUserId()).getNickname())
                .title(postInfo.getTitle())
                .content(postInfo.getContent())
                .uploadDate(formatLocalDate(postInfo.getUploadDate()))
                .views(postInfo.getViews())
                .permission(postInfo.getUserId().equals(userId))
                .build();

        PostReadModel.Book book = PostReadModel.Book.builder()
                .isbn(bookInfo.getIsbn())
                .title(bookInfo.getTitle())
                .image(bookInfo.getImage())
                .author(bookInfo.getAuthor() == null ? "작가 정보 없음" : bookInfo.getAuthor())
                .build();

        List<PostReadModel.Comment> commentList = commentInfoList.stream()
                .map(commentInfo ->
                        PostReadModel.Comment.builder()
                                .commentId(commentInfo.getCommentId())
                                .nickname(userService.getUserInfo(commentInfo.getUserId()).getNickname())
                                .content(commentInfo.getContent())
                                .uploadDate(formatLocalDate(commentInfo.getUploadDate()))
                                .permission(commentInfo.getUserId().equals(userId))
                                .build()
                )
                .toList();

        return PostReadModel.builder()
                .post(post)
                .book(book)
                .commentList(commentList)
                .build();
    }

    public BookSearchModel buildBookSearchModel(Page<BookInfo> page, String query) {
        List<BookSearchModel.Book> bookList = page.getContent().stream()
                .map(bookInfo ->
                        BookSearchModel.Book.builder()
                                .isbn(bookInfo.getIsbn())
                                .title(bookInfo.getTitle())
                                .image(bookInfo.getImage())
                                .author(bookInfo.getAuthor() == null ? "작가 정보 없음" : bookInfo.getAuthor())
                                .pubdate(bookInfo.getPubdate() == null ? "출간일 정보 없음" : formatLocalDate(bookInfo.getPubdate()))
                                .build()
                )
                .toList();

        Map<String, String> queryMap = new HashMap<>();
        if(query != null) queryMap.put("query", query);
        PageBarModel pageBarModel = buildPageBarModel(page, 10, "/books", queryMap);

        SearchBarModel searchBarModel = SearchBarModel.builder()
                .url("/books")
                .optionMap(null)
                .build();

        return BookSearchModel.builder()
                .bookList(bookList)
                .pageBarModel(pageBarModel)
                .searchBarModel(searchBarModel)
                .build();


    }

    public PostFormModel buildPostFormModel(BookInfo bookInfo, PostFormParam postFormParam, boolean alert) {
        return buildPostFormModel(bookInfo, postFormParam, alert,null);
    }

    public PostFormModel buildPostFormModel(BookInfo bookInfo, PostFormParam postFormParam, boolean alert, Long postId) {
        PostFormModel.Book book = PostFormModel.Book.builder()
                .bookInfo(bookInfo)
                .build();

        return PostFormModel.builder()
                .update(postId != null)
                .alert(alert)
                .book(book)
                .postId(postId)
                .title(postFormParam.getTitle() == null ? "" : postFormParam.getTitle())
                .content(postFormParam.getContent() == null ? "" : postFormParam.getContent())
                .build();
    }

    private PageBarModel buildPageBarModel(Page<?> page, int pageBarSize, String url, Map<String,String> queryMap) {
        int total = page.getTotalPages();
        int number = page.getNumber() + 1;
        int current, start, end;
        boolean prev, next;

        if(total == 0) {
            if(number == 1) {
                current = 1;
                prev = false;
                next = false;
                start = 1;
                end = 1;
            }
            else throw new RuntimeException("요소가 없을 땐 무조건 페이지 넘버가 1이여야합니다.");
        }
        else if(number > total) throw new RuntimeException("페이지 넘버가 최대값을 넘었습니다.");
        else {
            current = number;
            prev = number > pageBarSize;
            next = number <= ((total-1)/pageBarSize)*pageBarSize;
            start = ((number-1)/pageBarSize)*pageBarSize + 1;
            end = next ? start + pageBarSize - 1 : total;
        }

        return PageBarModel.builder()
                .url(url)
                .queryMap(queryMap)
                .current(current)
                .prev(prev)
                .next(next)
                .start(start)
                .end(end)
                .build();
    }



    private String formatLocalDate(LocalDate date) {
        return date.format(DateTimeFormatter.ofPattern("yyyy년 MM월 dd일"));
    }


}
