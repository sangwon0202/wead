package sangwon.wead.methodInterceptor;


import lombok.AllArgsConstructor;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;

@AllArgsConstructor
public class MethodInterceptorRegistry {

    private InterceptorRegistry registry;

    public MethodInterceptorRegistration addInterceptor(HandlerInterceptor interceptor) {
        MethodPathMatcher methodPathMatcher = new MethodPathMatcher();
        MethodInterceptor methodInterceptor = new MethodInterceptor(interceptor, methodPathMatcher);
        InterceptorRegistration registration = registry.addInterceptor(methodInterceptor);
        return new MethodInterceptorRegistration(methodPathMatcher, registration);
    }

}
