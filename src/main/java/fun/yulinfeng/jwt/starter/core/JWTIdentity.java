package fun.yulinfeng.jwt.starter.core;

import com.auth0.jwt.interfaces.DecodedJWT;

public abstract class JWTIdentity {

    public abstract Object getCurrent(DecodedJWT jwt);
}
