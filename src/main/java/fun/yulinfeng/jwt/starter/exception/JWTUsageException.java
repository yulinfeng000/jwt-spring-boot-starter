package fun.yulinfeng.jwt.starter.exception;

public class JWTUsageException extends JWTBaseException {

    public JWTUsageException(String message, Throwable cause) {
        super(message, cause);
    }

    public JWTUsageException(String message) {
        super(message);
    }
}
