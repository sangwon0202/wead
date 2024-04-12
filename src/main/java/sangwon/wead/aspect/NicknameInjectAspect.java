package sangwon.wead.aspect;

import lombok.RequiredArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;
import sangwon.wead.service.UserService;
import sangwon.wead.util.RequestProvider;


@Aspect
@Component
@RequiredArgsConstructor
public class NicknameInjectAspect {

    private final UserService userService;

    @Pointcut("execution(* sangwon.wead.controller.*.*(..))")
    private void allController() {}

    @Before("allController()")
    public void injectNickname(JoinPoint joinPoint) {
        String userId = RequestProvider.getUserIdFromSession();
        if(userId == null) return;

        Object[] args = joinPoint.getArgs();
        for (Object arg : args) {
            if(arg instanceof Model model) {
                String nickname = userService.getUserInfo(userId).getNickname();
                model.addAttribute("nickname",nickname);
            }
        }
    }
}
