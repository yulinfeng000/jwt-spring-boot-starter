package fun.yulinfeng.jwt.starter.core;

import com.auth0.jwt.interfaces.DecodedJWT;

public interface JWTIdentity {

    Object getCurrent(DecodedJWT jwt);
}
