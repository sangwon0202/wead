package sangwon.wead.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import sangwon.wead.interceptor.*;
import sangwon.wead.methodInterceptor.HttpMethod;
import sangwon.wead.methodInterceptor.MethodInterceptorRegistry;

@Configuration
@RequiredArgsConstructor
public class WebMvcConfig implements WebMvcConfigurer {

    private final NicknameInterceptor nicknameInterceptor;
    private final AlreadyLoginInterceptor alreadyLoginInterceptor;
    private final NotLoginInterceptor notLoginInterceptor;

    private final CheckExistenceInterceptor checkExistenceInterceptor;
    private final PermissionInterceptor permissionInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry interceptorRegistry) {
        MethodInterceptorRegistry registry = new MethodInterceptorRegistry(interceptorRegistry);

        registry.addInterceptor(notLoginInterceptor)
                .addPathPatterns("/logout")
                .addPathPatterns("/post/upload", HttpMethod.POST)
                .addPathPatterns("/post/update/**")
                .addPathPatterns("/post/delete/**")
                .addPathPatterns("/comment/delete/**")
                .order(1);

        registry.addInterceptor(alreadyLoginInterceptor)
                .addPathPatterns("/login")
                .addPathPatterns("/register")
                .order(2);


        registry.addInterceptor(checkExistenceInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns("/css/**")
                .order(3);

        registry.addInterceptor(permissionInterceptor)
                .addPathPatterns("/post/update/**")
                .addPathPatterns("/post/delete/**")
                .addPathPatterns("/comment/delete/**")
                .order(4);

        registry.addInterceptor(nicknameInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns("/css/**")
                .order(5);
    }

}
