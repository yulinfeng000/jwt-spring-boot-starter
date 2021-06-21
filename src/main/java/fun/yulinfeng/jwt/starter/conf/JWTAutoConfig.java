package fun.yulinfeng.jwt.starter.conf;

import com.auth0.jwt.algorithms.Algorithm;
import fun.yulinfeng.jwt.starter.algo.DefaultAlgorithmProvider;
import fun.yulinfeng.jwt.starter.algo.JWTAlgorithmProvider;
import fun.yulinfeng.jwt.starter.core.DefaultJWTIdentity;
import fun.yulinfeng.jwt.starter.core.JWTIdentity;
import fun.yulinfeng.jwt.starter.core.JWTManager;
import fun.yulinfeng.jwt.starter.enums.AlgoEnum;
import fun.yulinfeng.jwt.starter.exception.JWTConfigException;
import fun.yulinfeng.jwt.starter.interceptor.JWTAuthInterceptor;
import fun.yulinfeng.jwt.starter.resolver.JWTCurrentArgumentResolver;
import fun.yulinfeng.jwt.starter.property.JWTProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;


@Configuration
@EnableConfigurationProperties(JWTProperties.class)
public class JWTAutoConfig {

    @Autowired
    JWTProperties jwtProperties;

    @Bean
    @ConditionalOnMissingBean
    public JWTIdentity jwtIdentity() {
        return new DefaultJWTIdentity();
    }

    @Bean
    @ConditionalOnMissingBean
    public JWTManager jwtManager(JWTIdentity jwtIdentity) {
        return new JWTManager(jwtIdentity);
    }

    @Bean
    @ConditionalOnMissingBean
    public JWTAuthInterceptor jwtInterceptor(JWTManager jwtManager) {
        return new JWTAuthInterceptor(jwtManager, jwtProperties);
    }

    @Bean
    WebMvcConfigurer jwtWebMvcConfigurer(JWTAuthInterceptor jwtAuthInterceptor, JWTCurrentArgumentResolver jwtCurrentArgumentResolver) {
        return new JWTWebMvcConfigurer(jwtProperties, jwtAuthInterceptor, jwtCurrentArgumentResolver);
    }

    @Bean
    @ConditionalOnMissingBean
    JWTCurrentArgumentResolver jwtCurrentArgumentResolver(JWTIdentity jwtIdentity) {
        return new JWTCurrentArgumentResolver(jwtIdentity);
    }


    @Bean
    @ConditionalOnMissingBean
    JWTAlgorithmProvider defaultAlgorithmProvider(JWTProperties jwtProperties) {
        return new DefaultAlgorithmProvider(jwtProperties);
    }

    @Bean
    @ConditionalOnMissingBean
    Algorithm algorithm(JWTAlgorithmProvider provider) {
        return provider.algorithm();
    }
}
