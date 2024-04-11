package sangwon.wead.controller.advice;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import sangwon.wead.exception.ClientFaultException;
import sangwon.wead.exception.NeedLoginException;
import sangwon.wead.exception.NonexistentPostException;

import static sangwon.wead.util.AlertPageRedirector.redirectAlertPage;

@ControllerAdvice
@Slf4j
public class ExceptionAdvice {
    @ExceptionHandler(NeedLoginException.class)
    public String needLoginException(HttpServletRequest request, Model model) {
        String referer = request.getHeader("referer");
        return redirectAlertPage("로그인을 먼저 해주세요.", referer, model);
    }

    @ExceptionHandler(NonexistentPostException.class)
    public String nonexistentPostException(Model model) {
        return redirectAlertPage("존재하지 않는 게시글입니다.", "/", model);
    }

    @ExceptionHandler(ClientFaultException.class)
    public String clientFaultException() {
        return "redirect:/";
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public String typeMismatchException() {
        return "redirect:/";
    }

    @ExceptionHandler(RuntimeException.class)
    public String ServerException(Exception e, Model model) {
        log.error(e.getMessage());
        e.printStackTrace();
        return redirectAlertPage("서버 문제: 잠시 후 다시 시도해주세요.", "/", model);
    }
}
