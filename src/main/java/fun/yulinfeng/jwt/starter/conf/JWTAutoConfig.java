package fun.yulinfeng.jwt.starter.conf;

import com.auth0.jwt.algorithms.Algorithm;
import fun.yulinfeng.jwt.starter.core.JWTManager;
import fun.yulinfeng.jwt.starter.core.impl.DefaultAlgorithmProvider;
import fun.yulinfeng.jwt.starter.core.JWTAlgorithmProvider;
import fun.yulinfeng.jwt.starter.core.impl.DefaultJWTIdentity;
import fun.yulinfeng.jwt.starter.core.JWTIdentity;
import fun.yulinfeng.jwt.starter.core.impl.DefaultJWTManager;
import fun.yulinfeng.jwt.starter.interceptor.JWTAuthInterceptor;
import fun.yulinfeng.jwt.starter.resolver.JWTCurrentArgumentResolver;
import fun.yulinfeng.jwt.starter.property.JWTProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


@Configuration
@EnableConfigurationProperties(JWTProperties.class)
public class JWTAutoConfig {

    @Autowired
    public JWTProperties jwtProperties;

    @Bean
    @ConditionalOnMissingBean
    public JWTIdentity jwtIdentity() {
        return new DefaultJWTIdentity();
    }

    @Bean
    @ConditionalOnMissingBean
    public JWTManager jwtManager(JWTIdentity jwtIdentity, JWTProperties properties, Algorithm algorithm) {
        return new DefaultJWTManager(jwtIdentity, properties, algorithm);
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
