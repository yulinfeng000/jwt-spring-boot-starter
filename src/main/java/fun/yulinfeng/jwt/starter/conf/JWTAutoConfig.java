package fun.yulinfeng.jwt.starter.conf;

import com.auth0.jwt.algorithms.Algorithm;
import fun.yulinfeng.jwt.starter.core.DefaultJWTIdentity;
import fun.yulinfeng.jwt.starter.core.JWTIdentity;
import fun.yulinfeng.jwt.starter.core.JWTManager;
import fun.yulinfeng.jwt.starter.enums.AlgoEnum;
import fun.yulinfeng.jwt.starter.exception.JWTConfigException;
import fun.yulinfeng.jwt.starter.interceptor.JWTInterceptor;
import fun.yulinfeng.jwt.starter.property.JWTProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
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
    public JWTInterceptor jwtInterceptor(JWTManager jwtManager) {
        return new JWTInterceptor(jwtManager, jwtProperties);
    }

    @Bean
    WebMvcConfigurer jwtWebMvcConfigurer(JWTInterceptor jwtInterceptor) {
        return new JWTWebMvcConfigurer(jwtProperties, jwtInterceptor);
    }

    @Bean
    @ConditionalOnMissingBean
    Algorithm algorithm() throws InvocationTargetException, IllegalAccessException {
        //todo 目前只支持hash算法，还不支持keypair
        AlgoEnum algo = AlgoEnum.valueOf(jwtProperties.encryptMethod);
        try {
            Method method = Algorithm.class.getMethod(algo.algorithm, String.class);
            return (Algorithm) method.invoke(Algorithm.class, jwtProperties.secretKey);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
            throw new JWTConfigException("jwt算法配置错误", e.getCause());
        }
    }


}
