package sangwon.wead.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import sangwon.wead.config.ConfigurableInterceptor;

@Slf4j
@Component
public class ControllerMappingFailInterceptor implements ConfigurableInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if(handler.getClass() != HandlerMethod.class) {
            response.sendRedirect("/");
            return false;
        }
        return true;
    }

    @Override
    public void configure(InterceptorRegistration registration) {
        registration.excludePathPatterns("/css/**");
    }
}
