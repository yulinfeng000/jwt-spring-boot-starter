package fun.yulinfeng.jwt.starter.enums;

public enum AlgoEnum {
    HS256("HMAC256"),
    HS384("HMAC384"),
    HS512("HMAC512"),
    RS256("RSA256"),
    RS384("RSA384"),
    RS512("RSA512"),
    ES256("ECDSA256"),
    ES384("ECDSA384"),
    ES512("ECDSA512");

    public String algorithm;

    private AlgoEnum(String algo) {
        this.algorithm = algo;
    }


}
