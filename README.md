# jwt-spring-boot-starter
简单的jwt权限验证框架,面向使用前后端分离开发但又不想引入庞大安全框架的项目

## 使用方法

### 生成JWT
注入JWTManager,调用sign方法签发jwt令牌
```java
@RestController
public TestController {

    @Autowired
    JWTManager jwtManager;

    @PostMapping("/login")
    public Object login(User u){
        //TODO: check user login ...

        //if login success
        String token = jwtManager.sign(
            List.of("user","user:edit"), //自定义jwt的权限
            Map.of("id",u.id) //自定义jwt的携带信息,建议携带id,方便后面根据id查询当前用户
        )

        //TODO: return token ...
    }
}
```


### JWT拦截验证
在需要验证jwt的地方打上注解
1. 注解在方法上
    ```java
    @RestController
    public TestController {

        //必须携带jwt才可访问该方法
        @JWTRequire
        @GetMapping("/info")
        public Object info(){
            //TODO: return ...
        }

        //必须携带jwt并拥有对应权限才可访问
        @JWTRequire(role="user:edit")
        @PostMapping("/edit")    
        public Object edit(){
            //TODO: return ...
        }
    }
    ```
2. 注解在类上
    ```java
    @RestController
    @JWTRequire //注解在类上,该类的所有方法都必须携带jwt才可访问该方法
    public TestController {

        @GetMapping("/info")
        public Object info(){
            //TODO: return ...
        }

        @PostMapping("/edit")    
        public Object edit(){
            //TODO: return ...
        }


        //标明该方法不需要jwt验证
        @JWTRequire(required=false)
        public Object login(){
            //TODO: return ...
        }


        //jwt验证遵循就近原则,优先使用方法上注解配置,其次类上注解配置
        @JWTRequire(role="user")
        public Object remove(){
            //TODO: return ...
        }
    }
    ```

### 注入当前用户

1. 继承JWTIdentity,实现getCurrent方法

    ```java
    @Component
    public class MyJWTIdentity extends JWTIdentity{

        @Autowired
        UserService userService; //注入用户服务

        @Override
        Object getCurrent(DecodedJWT jwt){
            //根据当前请求的令牌实现查找并返回当前用户
            String id = jwt.getClaim("id")  //获得令牌中的id
            return userService.findById(id)
        }
    }
    ```

2. controller参数打上@JWTCurrent注解
    ```java
    @RestController
    public class TestController{

        @GetMapping("/current")
        @JWTRequire
        //参数打上@JWTCurrent注解，即可根据实现的getCurrent方法注入用户
        //该参数一定要和@JWTRequire共同使用,否则会抛出JWTUsageException错误
        public Object current(@JWTCurrent User u){
            return u;
        }
    }
    ```


### 发送请求
1. 请求头部携带token格式
    ```http
    Authorization: Bearer <your token here>
    ```

2. axios 整合示例
    ```js
    import axios from 'axios'

    const $axios = axios.create()

    $axios.interceptors.request.use((config) => {
        const token = getToken() //get token from cookies or ...
        config.headers['Authorization'] = `Bearer ${token}`
        return config
    })

    export default $axios
    ```


## TODO
1. 目前仅仅支持Hash算法
