package fun.yulinfeng.jwt.starter.core;


public class DefaultJWTIdentity implements JWTIdentity {
    @Override
    public Object getCurrent(String identity) {
        return identity;
    }
}
