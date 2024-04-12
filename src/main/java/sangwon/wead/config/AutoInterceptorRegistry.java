package sangwon.wead.config;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;

import java.util.List;

@Component
@RequiredArgsConstructor
public class AutoInterceptorRegistry {

    private final List<HandlerInterceptor> interceptors;

    public void register(InterceptorRegistry registry) {
        for(HandlerInterceptor interceptor : interceptors) {
            InterceptorRegistration registration = registry.addInterceptor(interceptor);
            if(interceptor instanceof ConfigurableInterceptor) {
                ((ConfigurableInterceptor) interceptor).configure(registration);
            }
        }
    }

}
