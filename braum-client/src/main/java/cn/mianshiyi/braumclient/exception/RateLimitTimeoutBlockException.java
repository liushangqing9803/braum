package cn.mianshiyi.braumclient.exception;

/**
 * @author shangqing.liu
 */
public class RateLimitTimeoutBlockException extends RuntimeException {

    private static final long serialVersionUID = -2924646262701911131L;

    public RateLimitTimeoutBlockException(String message) {
        super(message);
    }

    public RateLimitTimeoutBlockException(String message, Throwable cause) {
        super(message, cause);
    }

    public RateLimitTimeoutBlockException(Throwable cause) {
        super(cause);
    }

    public RateLimitTimeoutBlockException() {

    }
}
