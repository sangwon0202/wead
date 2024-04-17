package sangwon.wead.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import sangwon.wead.config.ConfigurableInterceptor;
import sangwon.wead.exception.ClientFaultException;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class UserExistenceCheckInterceptor implements ConfigurableInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Map<?, ?> pathVariables = (Map<?, ?>) request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);
        String userId = (String)pathVariables.get("userId");
        if(!userId.equals(request.getSession().getAttribute("userId"))) throw new ClientFaultException();
        return true;
    }

    @Override
    public void configure(InterceptorRegistration registration) {
        registration
                .addPathPatterns("/users/*")
                .addPathPatterns("/users/*/*")
                .excludePathPatterns("/users/register")
                .order(3);
    }

}
