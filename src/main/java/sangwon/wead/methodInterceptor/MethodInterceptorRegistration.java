package sangwon.wead.methodInterceptor;

import lombok.AllArgsConstructor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;

@AllArgsConstructor
public class MethodInterceptorRegistration {

    private MethodPathMatcher methodPathMatcher;

    private InterceptorRegistration registration;

    public MethodInterceptorRegistration order(int i) {
        registration.order(i);
        return this;
    }

    public MethodInterceptorRegistration addPathPatterns(String pattern) {
        return addPathPatterns(pattern, HttpMethod.ALL);
    }

    public MethodInterceptorRegistration excludePathPatterns(String pattern) {
        return excludePathPatterns(pattern, HttpMethod.ALL);
    }

    public MethodInterceptorRegistration addPathPatterns(String pattern, HttpMethod httpMethod) {
        methodPathMatcher.addInclude(pattern, httpMethod);
        return this;
    }

    public MethodInterceptorRegistration excludePathPatterns(String pattern, HttpMethod httpMethod) {
        methodPathMatcher.addExclude(pattern, httpMethod);
        return this;
    }
}
