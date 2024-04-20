package sangwon.wead.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import sangwon.wead.config.ConfigurableInterceptor;
import sangwon.wead.exception.NoLoginException;

@Component
public class LoginCheckInterceptor implements ConfigurableInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String userId = (String)request.getSession().getAttribute("userId");
        if(userId == null) throw new NoLoginException();
        return true;
    }

    @Override
    public void configure(InterceptorRegistration registration) {
        registration
                .addPathPatterns("/logout")
                .addPathPatterns("/books/*/posts/upload")
                .addPathPatterns("/posts/*/*")
                .addPathPatterns("/comments/*/*")
                .addPathPatterns("/users/*")
                .addPathPatterns("/users/*/*")
                .excludePathPatterns("/users/register")
                .order(1);
    }
}
