package sangwon.wead.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import sangwon.wead.config.ConfigurableInterceptor;
import sangwon.wead.service.PostService;
import sangwon.wead.service.UserService;


@Component
@RequiredArgsConstructor
public class CommonModelInterceptor implements ConfigurableInterceptor {

    private final UserService userService;
    private final PostService postService;

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) {
        if(modelAndView == null) return;
        String viewName = modelAndView.getViewName();
        if(viewName == null || viewName.equals("error") || viewName.contains("redirect:/")) return;

        String userId = (String)request.getSession().getAttribute("userId");
        if(userId != null) modelAndView.addObject("loginStatus", userService.getUser(userId));

        modelAndView.addObject("mostView", postService.getPostMostView());
    }

    @Override
    public void configure(InterceptorRegistration registration) {
        registration.order(2);
    }

}

