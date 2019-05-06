package timelogger.exceptions;

public class NotSeparatedTimesException extends RuntimeException {

    public NotSeparatedTimesException(String errorMessage) {
        super(errorMessage);
    }

    public NotSeparatedTimesException(String errorMessage, Throwable cause) {
        super(errorMessage, cause);
    }

}
