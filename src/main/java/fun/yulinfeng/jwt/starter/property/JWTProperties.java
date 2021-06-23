package fun.yulinfeng.jwt.starter.property;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "jwt")
public class JWTProperties {

    @Value("${jwt.expire-time:86400}")
    public Long expireTime;

    @Value("${jwt.secret-key:123456}")
    public String secretKey;

    @Value("${jwt.www.header:Authorization}")
    public String wwwHeader;

    @Value("${jwt.www.type:Bearer}")
    public String wwwType;

    @Value("${jwt.path.include:/**}")
    public String[] include;

    @Value("${jwt.path.exclude:}")
    public String[] exclude;

}
