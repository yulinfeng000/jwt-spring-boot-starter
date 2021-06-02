package fun.yulinfeng.jwt.starter;

import fun.yulinfeng.jwt.starter.core.JWTIdentity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TestJWTIdentity implements JWTIdentity {

    @Autowired
    MockDB db;

    @Override
    public Object getCurrent(String identity) {
        TestUser testUser = db.get(identity);
        return testUser;
    }
}
