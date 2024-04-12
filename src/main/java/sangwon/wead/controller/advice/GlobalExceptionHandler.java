package sangwon.wead.controller.advice;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ui.Model;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;
import sangwon.wead.exception.ClientFaultException;
import sangwon.wead.exception.NonexistentPostException;

import static sangwon.wead.util.AlertPageRedirector.redirectAlertPage;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(NonexistentPostException.class)
    public String nonexistentPostException(Model model) {
        return redirectAlertPage("존재하지 않는 게시글입니다.", "/posts", model);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public String methodArgumentTypeMismatchException(Model model) {
        return clientFaultException(model);
    }
    @ExceptionHandler(ClientFaultException.class)
    public String clientFaultException(Model model) {
        return redirectAlertPage("잘못된 접근입니다.", "/", model);
    }

    @ExceptionHandler(RuntimeException.class)
    public String runtimeException(Exception e, Model model) {
        log.error(e.getMessage());
        e.printStackTrace();
        return redirectAlertPage("서버 문제: 잠시 후 다시 시도해주세요.", "/", model);
    }
}
