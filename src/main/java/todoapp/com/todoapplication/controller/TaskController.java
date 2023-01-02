package todoapp.com.todoapplication.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import todoapp.com.todoapplication.exceptions.TaskAlreadyExistsException;
import todoapp.com.todoapplication.exceptions.TaskNotFoundException;
import todoapp.com.todoapplication.model.Task;
import todoapp.com.todoapplication.service.ITaskService;

import javax.naming.AuthenticationException;
import java.util.Map;

@RestController
@RequestMapping("/todo-service/v1")
public class TaskController {

    @Autowired
    private ITaskService taskService;

    @PostMapping("/create-task")
    public ResponseEntity<String> createTask(@RequestBody Task todoTask) {
        if (!validateTask(todoTask)) {
            return new ResponseEntity<>("TODO task is incomplete.", HttpStatus.BAD_REQUEST);
        }

        try {
            taskService.saveTask(todoTask);
        } catch (AuthenticationException e) {
            return new ResponseEntity<>("Authentication problem.", HttpStatus.FORBIDDEN);
        } catch (TaskAlreadyExistsException e) {
            return new ResponseEntity<>("Task with this ID already exists.", HttpStatus.CONFLICT);
        }
        return new ResponseEntity<>("Task was successfully created.", HttpStatus.OK);
    }

    @GetMapping("/list")
    public ResponseEntity<Map<Integer, Task>> retrieveAllTasks(@RequestParam Long userId) {
        try {
            return ResponseEntity.ok((taskService.getAllTasks(userId)));
        } catch (AuthenticationException e) {
            throw new RuntimeException(e);
        }
    }

    @PutMapping("/update-task")
    public ResponseEntity<String> updateTask(@RequestBody Task todoTask) {
        if (!validateTask(todoTask)) {
            return new ResponseEntity<>("TODO task is incomplete.", HttpStatus.BAD_REQUEST);
        }

        try {
            taskService.updateTask(todoTask);
        } catch (AuthenticationException e) {
            return new ResponseEntity<>("Authentication problem.", HttpStatus.FORBIDDEN);
        } catch (TaskNotFoundException e) {
            return new ResponseEntity<>("Task to update does not exist.", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>("Task was successfully updated.", HttpStatus.OK);
    }

    @DeleteMapping("/remove-task")
    public ResponseEntity<String> deleteTask(@RequestParam Long taskId, @RequestParam Long userId) {
        try {
            taskService.deleteTask(taskId, userId);
        } catch (AuthenticationException e) {
            return new ResponseEntity<>("Authentication problem.", HttpStatus.FORBIDDEN);
        } catch (TaskNotFoundException e) {
            return new ResponseEntity<>("Can't find task to remove.", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>("Task was successfully removed.", HttpStatus.OK);
    }

    private boolean validateTask(Task todoTask) {
        if (todoTask == null || todoTask.getTaskId() == null || todoTask.getTaskState() == null
                || todoTask.getDescription() == null || todoTask.getDeadline() == null) {
            return false;
        }
        return true;
    }

}
