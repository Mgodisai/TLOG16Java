package timelogger.exceptions;

public class NotNewMonthException extends RuntimeException {

    public NotNewMonthException(String errorMessage) {
        super(errorMessage);
    }

    public NotNewMonthException(String errorMessage, Throwable cause) {
        super(errorMessage, cause);
    }

}
