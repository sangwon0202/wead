package sangwon.wead.argument;

import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import sangwon.wead.argument.annotation.UserId;
import sangwon.wead.util.RequestProvider;


@Component
public class UserIdResolver implements HandlerMethodArgumentResolver {
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(UserId.class);
    }
    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        String userId = RequestProvider.getUserIdFromSession();
        if(userId == null && parameter.getParameterAnnotation(UserId.class).required()) throw new RuntimeException("세션에 userId가 저장되어 있지 않습니다");
        else return userId;
    }
}
