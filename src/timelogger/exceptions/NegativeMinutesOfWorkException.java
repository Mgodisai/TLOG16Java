package timelogger.exceptions;

public class NegativeMinutesOfWorkException extends RuntimeException {

    public NegativeMinutesOfWorkException(String errorMessage) {
        super(errorMessage);
    }

    public NegativeMinutesOfWorkException(String errorMessage, Throwable cause) {
        super(errorMessage, cause);
    }

}
