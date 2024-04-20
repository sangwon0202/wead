package sangwon.wead.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import sangwon.wead.config.ConfigurableInterceptor;
import sangwon.wead.exception.AlreadyLoginException;

@Component
public class LogoutCheckInterceptor implements ConfigurableInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String userId = (String)request.getSession().getAttribute("userId");
        if(userId != null) throw new AlreadyLoginException();
        return true;
    }

    @Override
    public void configure(InterceptorRegistration registration) {
        registration
                .addPathPatterns("/login")
                .addPathPatterns("/users/register")
                .order(1);
    }
}
