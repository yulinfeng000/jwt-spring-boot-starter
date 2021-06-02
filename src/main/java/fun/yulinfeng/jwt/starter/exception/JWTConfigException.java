package fun.yulinfeng.jwt.starter.exception;

public class JWTConfigException extends JWTBaseException{
    public JWTConfigException(String message, Throwable cause) {
        super(message, cause);
    }

    public JWTConfigException(String message) {
        super(message);
    }
}
