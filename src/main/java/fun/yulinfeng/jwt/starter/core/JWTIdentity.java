package fun.yulinfeng.jwt.starter.core;

public interface JWTIdentity {

    Object getCurrent(String identity);
}
