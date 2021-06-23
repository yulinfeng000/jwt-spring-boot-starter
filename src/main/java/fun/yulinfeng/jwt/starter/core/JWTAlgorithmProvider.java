package fun.yulinfeng.jwt.starter.core;

import com.auth0.jwt.algorithms.Algorithm;

public abstract class JWTAlgorithmProvider {

    public abstract Algorithm algorithm();
}
