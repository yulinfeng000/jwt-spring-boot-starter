package fun.yulinfeng.jwt.starter.resolver;

import com.auth0.jwt.interfaces.DecodedJWT;
import fun.yulinfeng.jwt.starter.annotation.JWTCurrent;
import fun.yulinfeng.jwt.starter.annotation.JWTRequire;
import fun.yulinfeng.jwt.starter.core.JWTIdentity;
import fun.yulinfeng.jwt.starter.exception.JWTUsageException;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;

import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;


public class JWTCurrentArgumentResolver implements HandlerMethodArgumentResolver {

    JWTIdentity jwtIdentity;

    public JWTCurrentArgumentResolver(JWTIdentity jwtIdentity) {
        this.jwtIdentity = jwtIdentity;
    }


    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        if (parameter.hasParameterAnnotation(JWTCurrent.class)) {
            if (parameter.getMethodAnnotation(JWTRequire.class) == null || parameter.getDeclaringClass().isAnnotationPresent(JWTRequire.class)) {
                throw new JWTUsageException("JWTCurrent注解必须在JWTRequire下使用!");
            }
        }
        return true;
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        DecodedJWT jwt = (DecodedJWT) webRequest.getAttribute("jwt", RequestAttributes.SCOPE_REQUEST);
        return jwtIdentity.getCurrent(jwt);
    }
}
