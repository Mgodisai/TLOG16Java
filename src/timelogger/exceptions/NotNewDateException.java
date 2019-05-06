package timelogger.exceptions;

public class NotNewDateException extends RuntimeException {

    public NotNewDateException(String errorMessage) {
        super(errorMessage);
    }

    public NotNewDateException(String errorMessage, Throwable cause) {
        super(errorMessage, cause);
    }

}
