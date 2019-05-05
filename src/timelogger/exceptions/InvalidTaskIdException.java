package timelogger.exceptions;

public class InvalidTaskIdException extends Exception {

    public InvalidTaskIdException(String errorMessage) {
        super(errorMessage);
    }

    public InvalidTaskIdException(String errorMessage, Throwable cause) {
        super(errorMessage, cause);
    }

}
