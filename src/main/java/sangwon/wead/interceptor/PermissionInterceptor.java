package sangwon.wead.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.HandlerMapping;
import sangwon.wead.service.CommentService;
import sangwon.wead.service.DTO.CommentInfo;
import sangwon.wead.service.DTO.PostInfo;
import sangwon.wead.service.PostService;

import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

@Component
@RequiredArgsConstructor
public class PermissionInterceptor implements HandlerInterceptor {

    private final PostService postService;
    private final CommentService commentService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        String userId = (String)request.getSession().getAttribute("userId");

        AtomicReference<Boolean> flag = new AtomicReference<>(false);
        Map<String, String> pathVariables = (Map<String, String>) request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);

        pathVariables.forEach((key, value) -> {
            if(key.equals("postId")) {
                Long postId = Long.parseLong(value);
                PostInfo postInfo = postService.getPostInfo(postId);
                if(!postInfo.getUserId().equals(userId)) flag.set(true);
            }
            if(key.equals("commentId")) {
                Long commentId = Long.parseLong(value);
                CommentInfo commentInfo = commentService.getCommentInfo(commentId);
                if(!commentInfo.getUserId().equals(userId)) flag.set(true);
            }
        });

        if(flag.get()) {
            response.sendRedirect("/");
            return false;
        }
        return true;
    }


}

