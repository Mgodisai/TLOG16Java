package timelogger.exceptions;

public class NotNewMonthException extends Exception {

    public NotNewMonthException(String errorMessage) {
        super(errorMessage);
    }

    public NotNewMonthException(String errorMessage, Throwable cause) {
        super(errorMessage, cause);
    }

}
