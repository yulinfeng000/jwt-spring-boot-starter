package fun.yulinfeng.jwt.starter;

import com.auth0.jwt.interfaces.DecodedJWT;
import fun.yulinfeng.jwt.starter.core.JWTIdentity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TestJWTIdentity extends JWTIdentity {

    @Autowired
    MockDB db;

    public Object getCurrent(DecodedJWT jwt) {
        String ide = jwt.getClaim("ide").asString();
        return db.get(ide);
    }
}
