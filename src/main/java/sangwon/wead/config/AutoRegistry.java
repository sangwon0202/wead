package sangwon.wead.config;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;

import java.util.List;

@Component
@RequiredArgsConstructor
public class AutoRegistry {

    private final List<HandlerInterceptor> interceptors;

    private final List<HandlerMethodArgumentResolver> argumentResolvers;

    public void registerArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        for(HandlerMethodArgumentResolver argumentResolver :this.argumentResolvers) {
            argumentResolvers.add(argumentResolver);
        }
    }

    public void registerInterceptors(InterceptorRegistry registry) {
        for(HandlerInterceptor interceptor : interceptors) {
            InterceptorRegistration registration = registry.addInterceptor(interceptor);
            if(interceptor instanceof ConfigurableInterceptor) {
                ((ConfigurableInterceptor) interceptor).configure(registration);
            }
        }
    }

}
