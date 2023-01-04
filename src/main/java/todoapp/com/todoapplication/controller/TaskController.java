package todoapp.com.todoapplication.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import todoapp.com.todoapplication.exceptions.RequestInputValidationException;
import todoapp.com.todoapplication.exceptions.TaskAlreadyExistsException;
import todoapp.com.todoapplication.exceptions.TaskNotFoundException;
import todoapp.com.todoapplication.exceptions.UserNotAuthenticatedException;
import todoapp.com.todoapplication.model.Task;
import todoapp.com.todoapplication.service.ITaskService;

@RestController
@RequestMapping("/todo-service/v1")
public class TaskController {

    @Autowired
    private ITaskService taskService;

    @PostMapping("/create-task")
    public ResponseEntity<String> createTask(@RequestBody Task todoTask) {
        try {
            return new ResponseEntity<>(taskService.saveTask(todoTask), HttpStatus.CREATED);
        } catch (RequestInputValidationException | JsonProcessingException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (UserNotAuthenticatedException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.FORBIDDEN);
        } catch (TaskAlreadyExistsException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
        }
    }

    @GetMapping("/list")
    public ResponseEntity<String> retrieveAllTasks(@RequestParam Long userId) {
        try {
            return ResponseEntity.ok((taskService.getAllTasks(userId)));
        } catch (UserNotAuthenticatedException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.FORBIDDEN);
        } catch (JsonProcessingException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/update-task")
    public ResponseEntity<String> updateTask(@RequestBody Task todoTask) {
        try {
            return new ResponseEntity<>(taskService.updateTask(todoTask), HttpStatus.OK);
        } catch (UserNotAuthenticatedException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.FORBIDDEN);
        } catch (TaskNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (RequestInputValidationException | JsonProcessingException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/remove-task")
    public ResponseEntity<String> deleteTask(@RequestParam Long taskId, @RequestParam Long userId) {
        try {
            taskService.deleteTask(taskId, userId);
        } catch (UserNotAuthenticatedException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.FORBIDDEN);
        } catch (TaskNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>("Task was successfully removed.", HttpStatus.OK);
    }

}
