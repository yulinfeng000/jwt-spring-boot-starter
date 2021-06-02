package fun.yulinfeng.jwt.starter.interceptor;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import fun.yulinfeng.jwt.starter.annotation.PermissionRequire;
import fun.yulinfeng.jwt.starter.core.JWTManager;
import fun.yulinfeng.jwt.starter.property.JWTProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class JWTAuthInterceptor implements HandlerInterceptor {


    @Autowired
    JWTManager jwtManager;


    JWTProperties jwtProperties;

    public JWTAuthInterceptor(JWTManager jwtManager, JWTProperties jwtProperties) {
        this.jwtManager = jwtManager;
        this.jwtProperties = jwtProperties;
    }

    private String getTokenStrFromRequest(HttpServletRequest request) {
        String header = request.getHeader(jwtProperties.wwwHeader);
        if (!StringUtils.hasText(header) || !header.startsWith(jwtProperties.wwwType))
            return null;
        return header.substring((jwtProperties.wwwType + " ").length());
    }

    private DecodedJWT checkPermission(PermissionRequire annotation, HttpServletRequest request) {
        String token = getTokenStrFromRequest(request);
        if (!StringUtils.hasText(token)) throw new JWTVerificationException("请求没有携带jwt-token");
        String role = annotation.role();
        if (StringUtils.hasText(role)) return jwtManager.verify(token, role);
        return jwtManager.verify(token);
    }

    private static void addIdentity(HttpServletRequest request, DecodedJWT jwt) {
        request.setAttribute("ide", jwt.getClaim("ide").asString());
    }

    private boolean process(PermissionRequire annotation, HttpServletRequest request) {
        DecodedJWT jwt = checkPermission(annotation, request);
        addIdentity(request, jwt);
        return true;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (!(handler instanceof HandlerMethod)) return true;
        PermissionRequire annotation = null;
        HandlerMethod method = (HandlerMethod) handler;

        // annotation from method
        annotation = method.getMethodAnnotation(PermissionRequire.class);
        if (annotation != null) {
            if (!annotation.required()) return true;
            return process(annotation, request);
        }

        // annotation from class
        annotation = method.getBean().getClass().getAnnotation(PermissionRequire.class);
        if (annotation != null) {
            if (!annotation.required()) return true;
            return process(annotation, request);
        }

        // no annotation
        return true;
    }
}
