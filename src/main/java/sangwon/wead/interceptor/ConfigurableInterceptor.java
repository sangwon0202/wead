package sangwon.wead.interceptor;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;

public interface ConfigurableInterceptor extends HandlerInterceptor {
    void configure(InterceptorRegistration registration);

}
