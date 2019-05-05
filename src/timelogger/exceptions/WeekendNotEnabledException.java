package timelogger.exceptions;

public class WeekendNotEnabledException extends Exception {

    public WeekendNotEnabledException(String errorMessage) {
        super(errorMessage);
    }

    public WeekendNotEnabledException(String errorMessage, Throwable cause) {
        super(errorMessage, cause);
    }

}
