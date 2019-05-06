package timelogger.exceptions;

public class WeekendNotEnabledException extends RuntimeException {

    public WeekendNotEnabledException(String errorMessage) {
        super(errorMessage);
    }

    public WeekendNotEnabledException(String errorMessage, Throwable cause) {
        super(errorMessage, cause);
    }

}
