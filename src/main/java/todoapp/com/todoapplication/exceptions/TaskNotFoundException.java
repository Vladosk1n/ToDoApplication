package todoapp.com.todoapplication.exceptions;

public class TaskNotFoundException extends RuntimeException {

    @Override
    public String getMessage() {
        return "This task can't be found.";
    }

}
