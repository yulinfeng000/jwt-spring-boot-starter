package fun.yulinfeng.jwt.starter.core.impl;


import com.auth0.jwt.interfaces.DecodedJWT;
import fun.yulinfeng.jwt.starter.core.JWTIdentity;

public class DefaultJWTIdentity extends JWTIdentity {

    public Object getCurrent(DecodedJWT jwt) {
        return jwt;
    }
}
