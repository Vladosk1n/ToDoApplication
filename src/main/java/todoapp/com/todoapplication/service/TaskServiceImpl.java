package todoapp.com.todoapplication.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import todoapp.com.todoapplication.dao.ITaskDao;
import todoapp.com.todoapplication.exceptions.TaskAlreadyExistsException;
import todoapp.com.todoapplication.exceptions.TaskNotFoundException;
import todoapp.com.todoapplication.model.Task;

import javax.naming.AuthenticationException;
import java.util.Map;

@Service
public class TaskServiceImpl implements ITaskService {

    @Autowired
    private ITaskDao taskDao;

    @Override
    public void saveTask(Task task) throws AuthenticationException, TaskAlreadyExistsException {
        if (task.getUserId() == null) {
            throw new AuthenticationException();
        }

        if (checkIfTaskAlreadyExists(task.getTaskId())) {
            throw new TaskAlreadyExistsException();
        }

        taskDao.saveTask(task);
    }

    @Override
    public void updateTask(Task task) throws AuthenticationException, TaskNotFoundException {
        if (task.getUserId() == null) {
            throw new AuthenticationException();
        }

        if (!checkIfTaskAlreadyExists(task.getTaskId())) {
            throw new TaskNotFoundException();
        }
        taskDao.updateTask(task);
    }

    @Override
    public Map<Integer, Task> getAllTasks(Long userId) throws AuthenticationException {
        if (userId == null) {
            throw new AuthenticationException();
        }

        return taskDao.getAllTasks();
    }

    @Override
    public void deleteTask(Long taskId, Long userId) throws AuthenticationException, TaskNotFoundException {
        if (userId == null) {
            throw new AuthenticationException();
        }

        if (!checkIfTaskAlreadyExists(taskId)) {
            throw new TaskNotFoundException();
        }
        taskDao.deleteTask(Math.toIntExact(taskId));
    }

    private boolean checkIfTaskAlreadyExists(Long taskId) {
        return taskDao.getOneTask(Math.toIntExact(taskId)) != null;
    }


}
