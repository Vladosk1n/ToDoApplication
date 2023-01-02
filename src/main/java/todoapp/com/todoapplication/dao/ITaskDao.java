package todoapp.com.todoapplication.dao;

import todoapp.com.todoapplication.model.Task;

import java.util.Map;

public interface ITaskDao {

    void saveTask(Task task);
    void updateTask(Task task);
    Task getOneTask(Integer taskId);
    Map<Integer, Task> getAllTasks();
    void deleteTask(Integer taskId);
}
