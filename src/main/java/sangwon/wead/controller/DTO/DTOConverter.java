package sangwon.wead.controller.DTO;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import sangwon.wead.service.CommentService;
import sangwon.wead.service.DTO.CommentInfo;
import sangwon.wead.service.DTO.PostInfo;
import sangwon.wead.service.UserService;
import sangwon.wead.service.book.BookService;


@Component
@RequiredArgsConstructor
public class DTOConverter {

    private final UserService userService;
    private final BookService bookService;
    private final CommentService commentService;


    public PostLine postInfoToPostLine(PostInfo postInfo) {
        String nickname = userService.getUserInfo(postInfo.getUserId()).getNickname();
        int commentCount = commentService.getCommentInfoList(postInfo.getPostId()).size();
        String image = bookService.getBookInfo(postInfo.getIsbn()).getImage();

        return PostLine.builder()
                .postInfo(postInfo)
                .nickname(nickname)
                .commentCount(commentCount)
                .image(image)
                .build();
    }

    public PostView postInfoToPostView(PostInfo postInfo, String userId) {
        String nickname = userService.getUserInfo(postInfo.getUserId()).getNickname();
        int commentCount = commentService.getCommentInfoList(postInfo.getPostId()).size();

        return PostView.builder()
                .postInfo(postInfo)
                .userId(userId)
                .nickname(nickname)
                .commentCount(commentCount)
                .build();
    }

    public CommentView commentInfoToCommentView(CommentInfo commentInfo, String userId) {
        String nickname = userService.getUserInfo(commentInfo.getUserId()).getNickname();
        return CommentView.builder()
                .commentInfo(commentInfo)
                .userId(userId)
                .nickname(nickname)
                .build();
    }

}
