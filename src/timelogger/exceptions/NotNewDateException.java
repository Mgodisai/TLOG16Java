package timelogger.exceptions;

public class NotNewDateException extends Exception {

    public NotNewDateException(String errorMessage) {
        super(errorMessage);
    }

    public NotNewDateException(String errorMessage, Throwable cause) {
        super(errorMessage, cause);
    }

}
