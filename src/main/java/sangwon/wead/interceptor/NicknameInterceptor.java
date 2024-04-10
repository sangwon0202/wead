package sangwon.wead.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import sangwon.wead.service.DTO.UserInfo;
import sangwon.wead.service.UserService;

@Component
@RequiredArgsConstructor
public class NicknameInterceptor implements HandlerInterceptor {

    private final UserService userService;
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        // GET 요청이고 redirect 가 아닐 경우, model 에 nickname 추가
        if(request.getMethod().equals("GET") && !modelAndView.getViewName().contains("redirect:/")) {
            HttpSession session = request.getSession();
            String userId = (String)session.getAttribute("userId");
            if(userId != null) {
                UserInfo userInfo = userService.getUserInfo(userId);
                modelAndView.addObject("nickname", userInfo.getNickname());
            }
        }
    }
}
