package sangwon.wead.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import sangwon.wead.service.DTO.UserDto;
import sangwon.wead.service.UserService;



@Component
@RequiredArgsConstructor
public class CommonModelInterceptor implements HandlerInterceptor {

    private final UserService userService;

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        if(modelAndView == null) return;
        String viewName = modelAndView.getViewName();
        if(viewName == null || viewName.equals("error") || viewName.contains("redirect:/")) return;

        String userId = (String)request.getSession().getAttribute("userId");
        modelAndView.addObject("loginStatus", userId != null);
        if(userId != null) modelAndView.addObject("userBox", userService.getUser(userId));
    }

}

