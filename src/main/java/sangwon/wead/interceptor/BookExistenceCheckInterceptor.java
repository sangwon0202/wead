package sangwon.wead.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import sangwon.wead.config.ConfigurableInterceptor;
import sangwon.wead.exception.ClientFaultException;
import sangwon.wead.service.book.search.BookSearchService;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class BookExistenceCheckInterceptor implements ConfigurableInterceptor {

    private final BookSearchService bookSearchService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Map<?, ?> pathVariables = (Map<?, ?>) request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);
        String isbn = (String)pathVariables.get("isbn");
        if(isbn == null) throw new RuntimeException("postId 가 path variable 안에 존재하지 않습니다.");
        if(!bookSearchService.checkBookExistence(isbn)) throw new ClientFaultException();
        return true;
    }

    @Override
    public void configure(InterceptorRegistration registration) {
        registration
                .addPathPatterns("/books/*")
                .order(2);
    }
}
