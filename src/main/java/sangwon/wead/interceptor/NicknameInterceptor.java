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
        // GET 요청일 때만 model 에 nickname 추가
        if(request.getMethod().equals("GET")) {
            HttpSession session = request.getSession();
            String userId = (String)session.getAttribute("userId");
            if(userId != null) {
                UserInfo userInfo = userService.getUserInfo(userId);
                modelAndView.getModel().put("nickname", userInfo.getNickname());
            }
        }
    }
}
