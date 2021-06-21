# jwt-spring-boot-starter
简单的jwt权限验证框架,面向使用前后端分离开发但又不想引入庞大安全框架的项目
jwt令牌生成并验证逻辑底层由auth0/java-jwt提供
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
注解在方法上
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
注解在类上
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

继承JWTIdentity,实现getCurrent方法

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

controller参数打上@JWTCurrent注解
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
### 自定义配置
请参考配置示例文件并根据自身需求配置
```yml
spring:
    port: xxx
    xx: xx 
    ....
jwt:
   expire-time: 86400 # jwt令牌过期时间,默认值86400,单位秒
   secret-key: mypasswd # jwt令牌hash加密key,默认值123456,强烈建议自定义key
   path:
      include: #jwt验证拦截路径,默认值 **/*
        - /user/*
        - /admin/*
      exclude: #jwt验证排除路径,默认值无
        - /public/*
   www:
      header: MyJWTAuthHeader # 自定义验证头,默认值:Authentication,不建议修改
      type: JWT  # 自定义令牌类型,默认:Bearer,不建议修改
```

如果需要更高级的加密算法,请继承JWTAlgorithmProvider并实现algorithm方法
```java
@Component
public class MyAlgorithmProvider extends JWTAlgorithmProvider{

    @Override
    public Algorithm algorithm(){
        //TODO：参考 https://github.com/auth0/java-jwt 实现对应算法
    } 
}

```
### 发送请求
请求头部携带token默认格式

```http request
Authorization: Bearer <your token here>
```

axios 整合示例

```javascript
import axios from 'axios'

const $axios = axios.create()

$axios.interceptors.request.use((config) => {
    const token = getToken() //get token from cookies or ...
    if(token)config.headers['Authorization'] = `Bearer ${token}`
    return config
})

export default $axios
```