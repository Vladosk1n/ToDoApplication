package todoapp.com.todoapplication.service;

import todoapp.com.todoapplication.exceptions.TaskAlreadyExistsException;
import todoapp.com.todoapplication.exceptions.TaskNotFoundException;
import todoapp.com.todoapplication.model.Task;

import javax.naming.AuthenticationException;
import java.util.Map;

public interface ITaskService {

    void saveTask(Task task) throws AuthenticationException, TaskAlreadyExistsException;
    void updateTask(Task task) throws AuthenticationException, TaskNotFoundException;
    Map<Integer, Task> getAllTasks(Long userId) throws AuthenticationException;
    void deleteTask(Long taskId, Long userId) throws AuthenticationException, TaskNotFoundException;
}
