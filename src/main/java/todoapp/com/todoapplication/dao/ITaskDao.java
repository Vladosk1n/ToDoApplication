package todoapp.com.todoapplication.dao;

import todoapp.com.todoapplication.model.Task;

import java.util.Map;

/**
 * Dao for working with Tasks
 */
public interface ITaskDao {

    /**
     * save Task to DB
     *
     * @param task task to save
     * @return saved task
     */
    Task saveTask(Task task);

    /**
     * update Task in DB
     *
     * @param task task to update
     * @return updated task
     */
    Task updateTask(Task task);

    /**
     * retrieve required Task from DB
     *
     * @param taskId ID of the task
     * @return retrieved task
     */
    Task getOneTask(Integer taskId);

    /**
     * retrieve all Tasks from DB
     *
     * @return all retrieved tasks in form <Key, Task>
     */
    Map<Integer, Task> getAllTasks();

    /**
     * delete Task from DB
     *
     * @param taskId ID of the task
     */
    void deleteTask(Integer taskId);

    /**
     * delete all Tasks from DB
     */
    void deleteAllTasks();
}
