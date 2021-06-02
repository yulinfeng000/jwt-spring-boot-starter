package fun.yulinfeng.jwt.starter;

import fun.yulinfeng.jwt.starter.annotation.PermissionRequire;
import fun.yulinfeng.jwt.starter.core.JWTManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Component
@RequestMapping("/user")
public class TestController {
    @Autowired
    MockDB mockDB;

    @Autowired
    JWTManager jwtManager;

    @PostMapping("/register")
    public Object register(@RequestBody RegisterForm form) {
        mockDB.add(new TestUser(form.username, form.password));
        return Map.of("code", 200, "msg", "success");
    }

    @PostMapping("/login")
    public Object login(@RequestBody LoginForm form) {
        TestUser user = mockDB.get(form.username);
        if (user == null) return Map.of("code", 401, "msg", "error");
        if (user.password.equals(form.password)) {
            String token = jwtManager.sign(List.of("user"), user.username);
            return Map.of("code", 200, "msg", "success", "token", token);
        } else return Map.of("code", 401, "msg", "error");
    }

    @PermissionRequire
    @GetMapping("/info")
    public Object info() {
        TestUser current = (TestUser) jwtManager.current();
        if (current != null) {
            return Map.of("user", current.username);
        }
        return Map.of("user", "");
    }
}
