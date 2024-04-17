package sangwon.wead.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import sangwon.wead.controller.model.CommonModel;
import sangwon.wead.service.DTO.UserInfo;
import sangwon.wead.service.UserService;


@Component
@RequiredArgsConstructor
@Slf4j
public class CommonModelInjectInterceptor implements HandlerInterceptor {

    private final UserService userService;

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        if(modelAndView == null) return;
        String viewName = modelAndView.getViewName();
        if(viewName == null || viewName.equals("error") || viewName.contains("redirect:/")) return;

        String userId = (String)request.getSession().getAttribute("userId");
        CommonModel commonModel;
        if(userId != null) {
            UserInfo userInfo = userService.getUserInfo(userId);
            commonModel = CommonModel.builder()
                    .login(true)
                    .userId(userId)
                    .nickname(userInfo.getNickname())
                    .build();
        }
        else {
            commonModel = CommonModel.builder()
                    .login(false)
                    .build();
        }
        modelAndView.addObject("commonModel",commonModel);
    }
}
