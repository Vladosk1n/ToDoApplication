package todoapp.com.todoapplication.exceptions;

public class RequestInputValidationException extends RuntimeException {

    @Override
    public String getMessage() {
        return "TODO task is incomplete.";
    }

}
