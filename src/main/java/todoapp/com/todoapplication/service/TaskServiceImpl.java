package todoapp.com.todoapplication.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import todoapp.com.todoapplication.dao.ITaskDao;
import todoapp.com.todoapplication.exceptions.RequestInputValidationException;
import todoapp.com.todoapplication.exceptions.TaskAlreadyExistsException;
import todoapp.com.todoapplication.exceptions.TaskNotFoundException;
import todoapp.com.todoapplication.exceptions.UserNotAuthenticatedException;
import todoapp.com.todoapplication.model.Task;

@Service
public class TaskServiceImpl implements ITaskService {

    @Autowired
    private ITaskDao taskDao;

    @Autowired
    ObjectMapper objectMapper;

    @Override
    public String saveTask(Task task) throws UserNotAuthenticatedException, TaskAlreadyExistsException, RequestInputValidationException, JsonProcessingException {
        userAuthentication(task.getUserId()); //authenticating user

        if (!validateTask(task)) { //validating request input
            throw new RequestInputValidationException();
        }

        if (checkIfTaskAlreadyExists(task.getTaskId())) {
            throw new TaskAlreadyExistsException();
        }

        // other business logic such as validation if deadline >= current date can be added here

        //returning saved task wrapped in json string
        return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(taskDao.saveTask(task));
    }

    @Override
    public String updateTask(Task task) throws UserNotAuthenticatedException, TaskNotFoundException, RequestInputValidationException, JsonProcessingException {
        userAuthentication(task.getUserId());

        if (!validateTask(task)) {
            throw new RequestInputValidationException();
        }

        if (!checkIfTaskAlreadyExists(task.getTaskId())) {
            throw new TaskNotFoundException();
        }
        return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(taskDao.updateTask(task));
    }

    @Override
    public String getAllTasks(Long userId) throws UserNotAuthenticatedException, JsonProcessingException {
        userAuthentication(userId);

        objectMapper.findAndRegisterModules(); //required to register the LocalDate datatype support
        return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(taskDao.getAllTasks());
    }

    @Override
    public void deleteTask(Long taskId, Long userId) throws UserNotAuthenticatedException, TaskNotFoundException {
        userAuthentication(userId);

        if (!checkIfTaskAlreadyExists(taskId)) {
            throw new TaskNotFoundException();
        }

        taskDao.deleteTask(Math.toIntExact(taskId));
    }

    private boolean checkIfTaskAlreadyExists(Long taskId) {
        return taskDao.getOneTask(Math.toIntExact(taskId)) != null;
    }

    private void userAuthentication(Long userId) throws UserNotAuthenticatedException {
        if (userId == null) {
            throw new UserNotAuthenticatedException();
        }
    }

    private boolean validateTask(Task todoTask) {
        if (todoTask == null || todoTask.getTaskId() == null || todoTask.getTaskState() == null
                || todoTask.getDescription() == null || todoTask.getDeadline() == null) {
            return false;
        }
        return true;
    }

}
