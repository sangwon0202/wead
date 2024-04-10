package sangwon.wead.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import sangwon.wead.exception.client.ClientException;

import static sangwon.wead.controller.util.AlertPageRedirector.redirectAlertPage;

@ControllerAdvice
@Slf4j
public class ExceptionAdvice {
    @ExceptionHandler(ClientException.class)
    public String clientException(Exception e, Model model) {
        return redirectAlertPage(e.getMessage(), "/", model);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public String TypeMismatchException(Model model) {
        return redirectAlertPage("잘못된 요청입니다.", "/", model);
    }

    @ExceptionHandler(RuntimeException.class)
    public String ServerException(Exception e, Model model) {
        log.error(e.getMessage());
        e.printStackTrace();
        return redirectAlertPage("서버 문제: 잠시 후 다시 시도해주세요.", "/", model);
    }



}
