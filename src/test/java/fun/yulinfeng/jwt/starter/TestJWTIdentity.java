package fun.yulinfeng.jwt.starter;

import com.auth0.jwt.interfaces.DecodedJWT;
import fun.yulinfeng.jwt.starter.core.JWTIdentity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TestJWTIdentity implements JWTIdentity {

    @Autowired
    MockDB db;

    @Override
    public Object getCurrent(DecodedJWT identity) {
        String ide = identity.getClaim("ide").asString();
        return db.get(ide);
    }
}
