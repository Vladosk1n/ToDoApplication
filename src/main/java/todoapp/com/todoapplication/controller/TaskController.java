package todoapp.com.todoapplication.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import todoapp.com.todoapplication.model.Task;
import todoapp.com.todoapplication.service.ITaskService;

import java.util.logging.Logger;

@RestController
@RequestMapping("/todo-service/v1")
public class TaskController {

    static Logger log = Logger.getLogger(TaskController.class.getName());

    @Autowired
    private ITaskService taskService;

    @PostMapping("/create-task")
    public ResponseEntity<String> createTask(@RequestBody Task todoTask) {
        //TODO: add log info and log errors
        return new ResponseEntity<>(taskService.saveTask(todoTask), HttpStatus.CREATED);
    }

    @GetMapping("/list")
    public ResponseEntity<String> retrieveAllTasks(@RequestParam Long userId) {
        return ResponseEntity.ok((taskService.getAllTasks(userId)));
    }

    @PutMapping("/update-task")
    public ResponseEntity<String> updateTask(@RequestBody Task todoTask) {
        return new ResponseEntity<>(taskService.updateTask(todoTask), HttpStatus.OK);
    }

    @DeleteMapping("/remove-task")
    public ResponseEntity<String> deleteTask(@RequestParam Long taskId, @RequestParam Long userId) {
        taskService.deleteTask(taskId, userId);
        return new ResponseEntity<>("Task was successfully removed.", HttpStatus.OK);
    }

}
