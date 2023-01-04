package todoapp.com.todoapplication.service;

import todoapp.com.todoapplication.exceptions.*;
import todoapp.com.todoapplication.model.Task;

/**
 * Service for working with Tasks
 */
public interface ITaskService {

    /**
     * save Task in repository
     *
     * @param task task to save
     * @return updated task
     * @throws UserNotAuthenticatedException   if userId is missing in request
     * @throws TaskAlreadyExistsException      if task with same taskId already exists
     * @throws RequestInputValidationException request is invalid
     * @throws MappingProblemException         if JSON mapping has failed
     */
    String saveTask(Task task) throws UserNotAuthenticatedException, TaskAlreadyExistsException, RequestInputValidationException, MappingProblemException;

    /**
     * update Task in repository
     *
     * @param task task to update
     * @return updated task
     * @throws UserNotAuthenticatedException   if userId is missing in request
     * @throws TaskNotFoundException           if task with requested taskId doesn't exist
     * @throws RequestInputValidationException request is invalid or JSON can't be parsed
     */
    String updateTask(Task task) throws UserNotAuthenticatedException, TaskNotFoundException, RequestInputValidationException, MappingProblemException;

    /**
     * return all Tasks from repository
     *
     * @param userId user ID to authenticate user
     * @return list of all tasks
     * @throws UserNotAuthenticatedException if userId is missing in request
     * @throws MappingProblemException       if response JSON can't be parsed
     */
    String getAllTasks(Long userId) throws UserNotAuthenticatedException, MappingProblemException;

    /**
     * delete Task from repository
     *
     * @param taskId task ID to delete
     * @param userId user ID to authenticate user
     * @throws UserNotAuthenticatedException if userId is missing in request
     * @throws TaskNotFoundException         if task with this taskId doesn't exist
     */
    void deleteTask(Long taskId, Long userId) throws UserNotAuthenticatedException, TaskNotFoundException;
}
