package fun.yulinfeng.jwt.starter.core;
import com.auth0.jwt.interfaces.DecodedJWT;

import java.time.Duration;
import java.util.List;
import java.util.Map;

public abstract class JWTManager {

    public abstract String sign(List<String> role, Map<String, Object> payload);
    public abstract String sign(List<String> role, Map<String, Object> payload, Duration expire);
    public abstract DecodedJWT verify(String token);
    public abstract DecodedJWT verify(String token, String role);

    @Deprecated(since = "建议使用注解方式注入用户")
    public abstract Object current();
}
