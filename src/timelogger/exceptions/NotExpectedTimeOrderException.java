package timelogger.exceptions;

public class NotExpectedTimeOrderException extends RuntimeException {

    public NotExpectedTimeOrderException(String errorMessage) {
        super(errorMessage);
    }

    public NotExpectedTimeOrderException(String errorMessage, Throwable cause) {
        super(errorMessage, cause);
    }

}
