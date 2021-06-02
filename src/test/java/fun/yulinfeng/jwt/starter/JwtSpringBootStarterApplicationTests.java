package fun.yulinfeng.jwt.starter;

import fun.yulinfeng.jwt.starter.core.JWTManager;

import fun.yulinfeng.jwt.starter.interceptor.JWTInterceptor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerExecutionChain;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.List;


import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


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
    private RequestMappingHandlerMapping handlerMapping;

    @Autowired
    JWTInterceptor jwtInterceptor;

    @Autowired
    TestController controller;

    @Autowired
    MockDB db;

    @Test
    void contextLoads() {
    }


    @Test
    void testJWTcurrent() throws Exception {
        TestUser testUser = new TestUser("123", "123");

        String token = jwtManager.sign(List.of("user"), testUser.username);
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setRequestURI("/user/info");
        request.setMethod("GET");
        request.addHeader("Authentication", "Bearer " + token);
        db.add(testUser);
        MockHttpServletResponse response = new MockHttpServletResponse();
        HandlerMethod info = new HandlerMethod(controller, TestController.class.getMethod("info"));
        boolean b = jwtInterceptor.preHandle(request, response, info);
        assert b;
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
        TestUser current = (TestUser) jwtManager.current();
        ModelAndView handle = handlerAdapter.handle(request, response, info);
        String user = (String) handle.getModel().get("user");
        assert user.equals("123");

    }

}
