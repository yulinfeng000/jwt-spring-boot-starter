package fun.yulinfeng.jwt.starter.core;


import com.auth0.jwt.interfaces.DecodedJWT;

public class DefaultJWTIdentity implements JWTIdentity {
    @Override
    public Object getCurrent(DecodedJWT identity) {
        return identity;
    }
}
