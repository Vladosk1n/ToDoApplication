package todoapp.com.todoapplication.exceptions;

public class RequestInputValidationException extends Exception {

    @Override
    public String getMessage() {
        return "TODO task is incomplete.";
    }

}
