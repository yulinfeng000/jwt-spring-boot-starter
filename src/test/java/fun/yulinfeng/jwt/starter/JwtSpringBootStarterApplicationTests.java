package fun.yulinfeng.jwt.starter;

import fun.yulinfeng.jwt.starter.core.JWTIdentity;
import fun.yulinfeng.jwt.starter.core.JWTManager;

import fun.yulinfeng.jwt.starter.interceptor.JWTAuthInterceptor;
import fun.yulinfeng.jwt.starter.resolver.JWTCurrentArgumentResolver;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;

import java.util.List;
import java.util.Map;


import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;


@SpringBootTest
@AutoConfigureMockMvc
class JwtSpringBootStarterApplicationTests {

    @Autowired
    WebApplicationContext wac;

    @Autowired
    JWTManager jwtManager;

    @Autowired
    MockMvc mockMvc;

    @Autowired
    private RequestMappingHandlerAdapter handlerAdapter;


    @Autowired
    JWTIdentity jwtIdentity;

    @Autowired
    private RequestMappingHandlerAdapter requestAdapter;

    @Autowired
    JWTAuthInterceptor jwtAuthInterceptor;


    @Autowired
    MockDB db;

    @Autowired
    TestController controller;

    @Test
    void contextLoads() {
    }


    @Test
    void testJWTManagercurrent() throws Exception {
        TestUser testUser = new TestUser("123", "123");

        String token = jwtManager.sign(List.of("user"), Map.of("ide",testUser.username));
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setRequestURI("/user/info");
        request.setMethod("GET");
        request.addHeader("Authorization", "Bearer " + token);
        db.add(testUser);
        MockHttpServletResponse response = new MockHttpServletResponse();
        HandlerMethod info = new HandlerMethod(controller, TestController.class.getMethod("info"));
        boolean b = jwtAuthInterceptor.preHandle(request, response, info);
        assert b;
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
        TestUser current = (TestUser) jwtManager.current();
        ModelAndView handle = handlerAdapter.handle(request, response, info);
        String user = (String) handle.getModel().get("user");
        assert user.equals("123");

    }

    @Test
    void testJWTCurrent() throws Exception {
        TestUser testUser = new TestUser("123", "123");

        String token = jwtManager.sign(List.of("user"), Map.of("ide",testUser.username));
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setRequestURI("/user/current");
        request.setMethod("GET");
        request.addHeader("Authorization", "Bearer " + token);
        db.add(testUser);
        MockHttpServletResponse response = new MockHttpServletResponse();
        HandlerMethod method = new HandlerMethod(controller, TestController.class.getMethod("testJWTCurrent", TestUser.class));
        boolean b = jwtAuthInterceptor.preHandle(request, response, method);
        assert b;
        requestAdapter.setArgumentResolvers(List.of(new JWTCurrentArgumentResolver(jwtIdentity)));
        ModelAndView handle = requestAdapter.handle(request, response, method);
        String user = (String) handle.getModel().get("user");
        assert user.equals("123");

    }

}
