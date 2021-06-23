package fun.yulinfeng.jwt.starter.core.impl;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import fun.yulinfeng.jwt.starter.core.JWTIdentity;
import fun.yulinfeng.jwt.starter.core.JWTManager;
import fun.yulinfeng.jwt.starter.property.JWTProperties;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DefaultJWTManager extends JWTManager {

    public JWTIdentity jwtIdentity;

    public JWTProperties jwtProperties;

    public Algorithm algorithm;

    public DefaultJWTManager(JWTIdentity identity, JWTProperties properties, Algorithm algorithm){
        this.jwtIdentity = identity;
        this.jwtProperties = properties;
        this.algorithm = algorithm;
    }

    public String sign(List<String> role, Map<String, Object> payload) {
        HashMap<String, Object> header = new HashMap<>();
        header.put("typ", "JWT");
        header.put("alg", algorithm.getName());
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime expire = now.plus(Duration.ofSeconds(jwtProperties.expireTime));
        return JWT.create()
                .withHeader(header)
                .withClaim("rle", role)
                .withPayload(payload)
                .withIssuedAt(Date.from(now.atZone(ZoneId.systemDefault()).toInstant()))
                .withExpiresAt(Date.from(expire.atZone(ZoneId.systemDefault()).toInstant()))
                .sign(algorithm);
    }

    public String sign(List<String> role, Map<String, Object> payload, Duration expire) {
        HashMap<String, Object> header = new HashMap<>();
        header.put("typ", "JWT");
        header.put("alg", algorithm.getName());
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime expireTime = now.plus(expire);
        return JWT.create()
                .withHeader(header)
                .withClaim("rle", role)
                .withPayload(payload)
                .withIssuedAt(Date.from(now.atZone(ZoneId.systemDefault()).toInstant()))
                .withExpiresAt(Date.from(expireTime.atZone(ZoneId.systemDefault()).toInstant()))
                .sign(algorithm);
    }

    public DecodedJWT verify(String token) {
        return JWT.require(algorithm).build().verify(token);
    }

    public DecodedJWT verify(String token, String role) {
        DecodedJWT jwt =  JWT.require(algorithm).build().verify(token);
        List<String> rle = jwt.getClaim("rle").asList(String.class);
        if (rle != null && rle.contains(role))
            return jwt;
        throw new JWTVerificationException("无目标权限");
    }

    @Deprecated(since = "建议使用注解方式注入用户")
    public Object current() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        DecodedJWT ide = (DecodedJWT) request.getAttribute("jwt");
        return jwtIdentity.getCurrent(ide);
    }
}
