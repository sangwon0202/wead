package sangwon.wead.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import sangwon.wead.resolover.RefererResolver;
import sangwon.wead.resolover.UserIdResolver;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class WebMvcConfig implements WebMvcConfigurer {

    private final AutoInterceptorRegistry autoRegistry;
    private final RefererResolver refererResolver;
    private final UserIdResolver userIdResolver;


    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        autoRegistry.register(registry);
    }
    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(refererResolver);
        resolvers.add(userIdResolver);
    }

}
