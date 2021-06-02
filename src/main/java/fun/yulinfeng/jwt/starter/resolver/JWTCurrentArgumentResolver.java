package fun.yulinfeng.jwt.starter.resolver;

import fun.yulinfeng.jwt.starter.annotation.JWTCurrent;
import fun.yulinfeng.jwt.starter.annotation.PermissionRequire;
import fun.yulinfeng.jwt.starter.core.JWTIdentity;
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
        return parameter.getMethodAnnotation(PermissionRequire.class) != null && parameter.hasParameterAnnotation(JWTCurrent.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        String ide = (String) webRequest.getAttribute("ide", RequestAttributes.SCOPE_REQUEST);
        return jwtIdentity.getCurrent(ide);
    }
}
