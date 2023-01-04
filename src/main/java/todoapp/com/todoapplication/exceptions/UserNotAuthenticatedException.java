package todoapp.com.todoapplication.exceptions;

public class UserNotAuthenticatedException extends RuntimeException {

    @Override
    public String getMessage() {
        return "Authentication problem.";
    }

}
