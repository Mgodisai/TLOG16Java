package timelogger.exceptions;

public class EmptyTimeFieldException extends Exception {

    public EmptyTimeFieldException(String errorMessage) {
        super(errorMessage);
    }

    public EmptyTimeFieldException(String errorMessage, Throwable cause) {
        super(errorMessage, cause);
    }

}
