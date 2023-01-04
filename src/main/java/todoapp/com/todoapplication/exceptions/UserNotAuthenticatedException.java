package todoapp.com.todoapplication.exceptions;

public class UserNotAuthenticatedException extends Exception {

    @Override
    public String getMessage() {
        return "Authentication problem.";
    }

}
