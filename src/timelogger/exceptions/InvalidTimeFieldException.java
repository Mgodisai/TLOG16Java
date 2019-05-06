package timelogger.exceptions;

public class InvalidTimeFieldException extends RuntimeException {

    public InvalidTimeFieldException(String errorMessage) {
        super(errorMessage);
    }

    public InvalidTimeFieldException(String errorMessage, Throwable cause) {
        super(errorMessage, cause);
    }

}