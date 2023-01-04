package todoapp.com.todoapplication.exceptions;

public class TaskNotFoundException extends Exception {

    @Override
    public String getMessage() {
        return "This task can't be found.";
    }

}
