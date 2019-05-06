package timelogger.exceptions;

public class InvalidTaskIdException extends RuntimeException {

    public InvalidTaskIdException(String errorMessage) {
        super(errorMessage);
    }

    public InvalidTaskIdException(String errorMessage, Throwable cause) {
        super(errorMessage, cause);
    }

}
