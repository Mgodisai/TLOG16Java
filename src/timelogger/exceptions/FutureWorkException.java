package timelogger.exceptions;

public class FutureWorkException extends RuntimeException {

    public FutureWorkException(String errorMessage) {
        super(errorMessage);
    }

    public FutureWorkException(String errorMessage, Throwable cause) {
        super(errorMessage, cause);
    }

}
