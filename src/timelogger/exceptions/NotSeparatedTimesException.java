package timelogger.exceptions;

public class NotSeparatedTimesException extends Exception {

    public NotSeparatedTimesException(String errorMessage) {
        super(errorMessage);
    }

    public NotSeparatedTimesException(String errorMessage, Throwable cause) {
        super(errorMessage, cause);
    }

}
