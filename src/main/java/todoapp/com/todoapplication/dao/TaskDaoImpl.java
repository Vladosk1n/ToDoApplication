package todoapp.com.todoapplication.dao;

import jakarta.annotation.Resource;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.stereotype.Repository;
import todoapp.com.todoapplication.model.Task;

import java.util.List;
import java.util.Map;

@Repository
public class TaskDaoImpl implements ITaskDao {

    private final String hashReference = "Task";

    @Resource(name = "redisTemplate") // 'redisTemplate' is defined as a Bean in AppConfig.java
    private HashOperations<String, Integer, Task> hashOperations;

    @Override
    public Task saveTask(Task task) {
        hashOperations.putIfAbsent(hashReference, Math.toIntExact(task.getTaskId()), task);
        return task;

    }

    @Override
    public Task updateTask(Task task) {
        hashOperations.put(hashReference, Math.toIntExact(task.getTaskId()), task);
        return task;
    }

    @Override
    public Task getOneTask(Integer taskId) {
        return hashOperations.get(hashReference, taskId);
    }

    @Override
    public Map<Integer, Task> getAllTasks() {
        return hashOperations.entries(hashReference);
    }

    @Override
    public void deleteTask(Integer taskId) {
        hashOperations.delete(hashReference, taskId);
    }

    @Override
    public void deleteAllTasks() {
        List<Long> tasksId = getAllTasks().values().stream().map(Task::getTaskId).toList();
        for (long taskId : tasksId) {
            deleteTask(Math.toIntExact(taskId));
        }
    }

}
