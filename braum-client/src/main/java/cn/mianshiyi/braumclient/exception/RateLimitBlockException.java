package cn.mianshiyi.braumclient.exception;

/**
 * @author shangqing.liu
 */
public class RateLimitBlockException extends RuntimeException {

    private static final long serialVersionUID = -2924646262701911134L;

    public RateLimitBlockException(String message) {
        super(message);
    }

    public RateLimitBlockException(String message, Throwable cause) {
        super(message, cause);
    }

    public RateLimitBlockException(Throwable cause) {
        super(cause);
    }

    public RateLimitBlockException() {

    }
}
