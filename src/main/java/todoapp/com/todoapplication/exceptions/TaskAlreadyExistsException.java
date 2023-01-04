package todoapp.com.todoapplication.exceptions;

public class TaskAlreadyExistsException extends RuntimeException {

    @Override
    public String getMessage() {
        return "Task with this ID already exists.";
    }

}
