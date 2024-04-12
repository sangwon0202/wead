package sangwon.wead.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import sangwon.wead.config.ConfigurableInterceptor;
import sangwon.wead.exception.ClientFaultException;
import sangwon.wead.exception.NonexistentPostException;
import sangwon.wead.service.PostService;

import java.util.Map;

@Component
@RequiredArgsConstructor
@Slf4j
public class PostExistenceCheckInterceptor implements ConfigurableInterceptor {

    private final PostService postService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Map<?, ?> pathVariables = (Map<?, ?>) request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);
        String temp = (String)pathVariables.get("postId");
        if(temp == null) throw new RuntimeException("path variable 에 postId가 존재하지 않습니다.");
        Long postId;
        try {
            postId = Long.parseLong(temp);
        }
        catch (NumberFormatException e) {
            throw new ClientFaultException();
        }
        if(!postService.checkPostExistence(postId)) throw new NonexistentPostException();
        return true;
    }

    @Override
    public void configure(InterceptorRegistration registration) {
        registration
                .addPathPatterns("/posts/*")
                .order(2);
    }
}
