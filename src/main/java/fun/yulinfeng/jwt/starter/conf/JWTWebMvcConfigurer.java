package fun.yulinfeng.jwt.starter.conf;

import fun.yulinfeng.jwt.starter.interceptor.JWTAuthInterceptor;
import fun.yulinfeng.jwt.starter.resolver.JWTCurrentArgumentResolver;
import fun.yulinfeng.jwt.starter.property.JWTProperties;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

public class JWTWebMvcConfigurer implements WebMvcConfigurer {
    JWTProperties jwtProperties;
    JWTAuthInterceptor jwtAuthInterceptor;
    JWTCurrentArgumentResolver jwtCurrentArgumentResolver;

    JWTWebMvcConfigurer(JWTProperties jwtProperties, JWTAuthInterceptor jwtAuthInterceptor, JWTCurrentArgumentResolver jwtCurrentArgumentResolver) {
        this.jwtAuthInterceptor = jwtAuthInterceptor;
        this.jwtProperties = jwtProperties;
        this.jwtCurrentArgumentResolver = jwtCurrentArgumentResolver;
    }

    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(jwtAuthInterceptor)
                .addPathPatterns(jwtProperties.include)
                .excludePathPatterns(jwtProperties.exclude);
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(jwtCurrentArgumentResolver);
    }
}
