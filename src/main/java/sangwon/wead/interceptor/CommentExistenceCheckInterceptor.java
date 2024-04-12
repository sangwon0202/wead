package sangwon.wead.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import sangwon.wead.config.ConfigurableInterceptor;
import sangwon.wead.exception.ClientFaultException;
import sangwon.wead.service.CommentService;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class CommentExistenceCheckInterceptor implements ConfigurableInterceptor {

    private final CommentService commentService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Map<?, ?> pathVariables = (Map<?, ?>) request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);
        String temp = (String)pathVariables.get("commentId");
        if(temp == null) throw new RuntimeException("path variable 에 commentId가 존재하지 않습니다.");
        Long commentId;
        try {
            commentId = Long.parseLong(temp);
        }
        catch (NumberFormatException e) {
            throw new ClientFaultException();
        }
        if(!commentService.checkCommentExistence(commentId)) throw new ClientFaultException();
        return true;
    }

    @Override
    public void configure(InterceptorRegistration registration) {
        registration
                .addPathPatterns("/comments/*")
                .order(2);
    }
}
