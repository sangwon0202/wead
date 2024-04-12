package sangwon.wead.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import sangwon.wead.service.UserService;


@Component
@RequiredArgsConstructor
@Slf4j
public class LoginStatusInjectInterceptor implements HandlerInterceptor {

    private final UserService userService;

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        if(modelAndView == null) return;
        String viewName = modelAndView.getViewName();
        if(viewName.equals("error") || viewName.contains("redirect:/")) return;
        String userId = (String)request.getSession().getAttribute("userId");
        if(userId != null) {
            modelAndView.addObject("login", true);
            modelAndView.addObject("nickname", userService.getUserInfo(userId).getNickname());
        }
        else modelAndView.addObject("login", false);
    }

}
