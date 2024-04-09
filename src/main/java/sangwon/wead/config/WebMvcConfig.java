package sangwon.wead.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import sangwon.wead.interceptor.NicknameInterceptor;

@Configuration
@RequiredArgsConstructor
public class WebMvcConfig implements WebMvcConfigurer {

    private final NicknameInterceptor nicknameInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(nicknameInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns("/css/**"); // css 제외

    }

}
