package sangwon.wead.interceptor;


import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.HandlerMapping;
import sangwon.wead.service.CommentService;
import sangwon.wead.service.PostService;
import sangwon.wead.service.book.BookService;

import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;


@Component
@RequiredArgsConstructor
public class CheckExistenceInterceptor implements HandlerInterceptor {

    private final PostService postService;
    private final CommentService commentService;
    private final BookService bookService;
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        AtomicReference<Boolean> flag = new AtomicReference<>(false);
        Map<String, String> pathVariables = (Map<String, String>) request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);

        pathVariables.forEach((key, value) -> {
            try {
                if(key.equals("postId")) {
                    Long postId = Long.parseLong(value);
                    if(!postService.checkPostExistence(postId)) flag.set(true);
                }
                if(key.equals("commentId")) {
                    Long commentId = Long.parseLong(value);
                    if(!commentService.checkCommentExistence(commentId)) flag.set(true);
                }
            }
            catch (NumberFormatException e) {
                flag.set(true);
            }
        });

        String isbn = request.getParameter("isbn");
        if(isbn != null && !bookService.checkBookExistence(isbn)) flag.set(true);

        if(flag.get()) {
            response.sendRedirect("/");
            return false;
        }
        return true;
    }
}
