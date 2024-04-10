package sangwon.wead.methodInterceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;


@RequiredArgsConstructor
public class MethodInterceptor implements HandlerInterceptor {

    private final HandlerInterceptor interceptor;
    private final MethodPathMatcher pathMatcher;
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if(pathMatcher.match(request.getRequestURI(), request.getMethod())) {
            return interceptor.preHandle(request, response, handler);
        }
        else return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        if(pathMatcher.match(request.getRequestURI(), request.getMethod())) {
            interceptor.postHandle(request, response, handler, modelAndView);
        }
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        if(pathMatcher.match(request.getRequestURI(), request.getMethod())) {
            interceptor.afterCompletion(request, response, handler, ex);
        }
    }
}
