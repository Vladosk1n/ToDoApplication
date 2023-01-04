package todoapp.com.todoapplication.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import todoapp.com.todoapplication.dao.ITaskDao;
import todoapp.com.todoapplication.exceptions.*;
import todoapp.com.todoapplication.model.Task;

import java.util.logging.Logger;

@Service
public class TaskServiceImpl implements ITaskService {

    static Logger log = Logger.getLogger(TaskServiceImpl.class.getName());

    @Autowired
    private ITaskDao taskDao;

    @Autowired
    ObjectMapper objectMapper;

    @Override
    public String saveTask(Task task) throws UserNotAuthenticatedException, TaskAlreadyExistsException, RequestInputValidationException, MappingProblemException {
        userAuthentication(task.getUserId()); //authenticating user

        if (!validateTask(task)) { //validating request input
            throw new RequestInputValidationException();
        }

        if (checkIfTaskAlreadyExists(task.getTaskId())) {
            throw new TaskAlreadyExistsException();
        }

        // other business logic such as validation if deadline >= current date can be added here

        //returning saved task wrapped in json string
        try {
            return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(taskDao.saveTask(task));
        } catch (JsonProcessingException e) {
            throw new MappingProblemException();
        }
    }

    @Override
    public String updateTask(Task task) throws UserNotAuthenticatedException, TaskNotFoundException, RequestInputValidationException, MappingProblemException {
        userAuthentication(task.getUserId());

        if (!validateTask(task)) {
            throw new RequestInputValidationException();
        }

        if (!checkIfTaskAlreadyExists(task.getTaskId())) {
            throw new TaskNotFoundException();
        }

        try {
            return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(taskDao.updateTask(task));
        } catch (JsonProcessingException e) {
            throw new MappingProblemException();
        }
    }

    @Override
    public String getAllTasks(Long userId) throws UserNotAuthenticatedException, MappingProblemException {
        userAuthentication(userId);

        objectMapper.findAndRegisterModules(); //required to register the LocalDate datatype support
        try {
            return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(taskDao.getAllTasks());
        } catch (JsonProcessingException e) {
            throw new MappingProblemException();
        }
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
        log.info("Checking if task already exists: taskId= " + taskId);
        return taskDao.getOneTask(Math.toIntExact(taskId)) != null;
    }

    private void userAuthentication(Long userId) throws UserNotAuthenticatedException {
        log.info("Authenticating of user: userId= " + userId);

        if (userId == null) {
            log.warning("User authentication has failed.");
            throw new UserNotAuthenticatedException();
        }
    }

    private boolean validateTask(Task todoTask) {
        log.info("Validating the task: taskId= " + todoTask.getTaskId());

        if (todoTask.getTaskId() == null || todoTask.getTaskState() == null || todoTask.getDescription() == null || todoTask.getDeadline() == null) {
            log.warning("Task validation has failed.");
            return false;
        }
        return true;
    }

}
