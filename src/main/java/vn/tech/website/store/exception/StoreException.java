package vn.tech.website.store.exception;

public class StoreException extends Exception {
    private String message;

    public StoreException(String message) {
        super(message);
        this.message = message;
    }

    public StoreException(String message, Throwable cause) {
        super(message, cause);
        this.message = message;
    }

    public StoreException(Throwable cause) {
        super(cause);
    }

    public StoreException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
        this.message = message;
    }
}
