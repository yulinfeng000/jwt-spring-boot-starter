package fun.yulinfeng.jwt.starter.exception;

public class JWTBaseException extends RuntimeException {

    public JWTBaseException(String message, Throwable cause) {
        super(message, cause);
    }

    public JWTBaseException(String message) {
        super(message);
    }
}
