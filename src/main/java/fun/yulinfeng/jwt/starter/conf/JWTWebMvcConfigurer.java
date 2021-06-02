package fun.yulinfeng.jwt.starter.conf;

import fun.yulinfeng.jwt.starter.interceptor.JWTInterceptor;
import fun.yulinfeng.jwt.starter.property.JWTProperties;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

public class JWTWebMvcConfigurer implements WebMvcConfigurer {
    JWTProperties jwtProperties;
    JWTInterceptor jwtInterceptor;

    JWTWebMvcConfigurer(JWTProperties jwtProperties, JWTInterceptor jwtInterceptor) {
        this.jwtInterceptor = jwtInterceptor;
        this.jwtProperties = jwtProperties;
    }

    public void addInterceptors(InterceptorRegistry registry) {
        WebMvcConfigurer.super.addInterceptors(registry);
        registry.addInterceptor(jwtInterceptor)
                .addPathPatterns(jwtProperties.include)
                .excludePathPatterns(jwtProperties.exclude);
    }
}
