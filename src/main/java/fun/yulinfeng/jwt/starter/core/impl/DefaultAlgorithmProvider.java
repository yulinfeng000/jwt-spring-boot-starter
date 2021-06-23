package fun.yulinfeng.jwt.starter.core.impl;

import com.auth0.jwt.algorithms.Algorithm;
import fun.yulinfeng.jwt.starter.core.JWTAlgorithmProvider;
import fun.yulinfeng.jwt.starter.property.JWTProperties;

public class DefaultAlgorithmProvider extends JWTAlgorithmProvider {

    JWTProperties jwtProperties;

    public DefaultAlgorithmProvider(JWTProperties jwtProperties){
        this.jwtProperties = jwtProperties;
    }

    @Override
    public Algorithm algorithm() {
        return Algorithm.HMAC256(jwtProperties.secretKey);
    }
}
