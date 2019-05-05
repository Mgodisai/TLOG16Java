package timelogger.exceptions;

public class NoTaskIdException extends Exception {

    public NoTaskIdException(String errorMessage) {
        super(errorMessage);
    }

    public NoTaskIdException(String errorMessage, Throwable cause) {
        super(errorMessage, cause);
    }

}
