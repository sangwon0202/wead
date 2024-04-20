package sangwon.wead.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import sangwon.wead.resolover.RefererResolver;
import sangwon.wead.resolover.UserIdResolver;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class WebMvcConfig implements WebMvcConfigurer {

    private final RefererResolver refererResolver;
    private final UserIdResolver userIdResolver;
    private final List<HandlerInterceptor> interceptors;


    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        interceptors.forEach(interceptor -> {
            InterceptorRegistration registration = registry.addInterceptor(interceptor);
                if(interceptor instanceof ConfigurableInterceptor)
                    ((ConfigurableInterceptor) interceptor).configure(registration);
            });
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(refererResolver);
        resolvers.add(userIdResolver);
    }

}
