package fun.yulinfeng.jwt.starter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


import java.util.concurrent.ConcurrentHashMap;

@Component
public class MockDB {

    public static final ConcurrentHashMap<String, TestUser> db = new ConcurrentHashMap<>();

    public void add(TestUser user) {
        db.put(user.username, user);
    }

    public TestUser get(String username) {
        return db.get(username);
    }
}
