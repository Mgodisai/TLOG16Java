package timelogger.exceptions;

public class NotTheSameMonthException extends Exception {

    public NotTheSameMonthException(String errorMessage) {
        super(errorMessage);
    }

    public NotTheSameMonthException(String errorMessage, Throwable cause) {
        super(errorMessage, cause);
    }

}
