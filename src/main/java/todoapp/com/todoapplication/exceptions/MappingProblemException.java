package todoapp.com.todoapplication.exceptions;

public class MappingProblemException extends RuntimeException {

    @Override
    public String getMessage() {
        return "Response mapping to JSON has failed.";
    }

}
