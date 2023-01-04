package todoapp.com.todoapplication;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import todoapp.com.todoapplication.dao.ITaskDao;
import todoapp.com.todoapplication.model.Task;
import todoapp.com.todoapplication.model.enums.TaskState;

import java.time.LocalDate;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class TaskServiceTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private ITaskDao taskDao;

    @BeforeEach
        //run it before each test to flush existing DB records
    void deleteAllFromTheTable() {
        taskDao.deleteAllTasks();
    }

    @Test
    void createNewTaskSuccessfullyCreatedCase() throws Exception {

        Task task = new Task(1L, 1L, "Task description 1.", LocalDate.now(), TaskState.TODO);

        //make a request
        mockMvc.perform(MockMvcRequestBuilders.post("/todo-service/v1/create-task")
                        .content(objectMapper.writeValueAsString(task))
                        .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());//verify response status

        Task taskFromDao = taskDao.getOneTask(1);

        assertThat(taskFromDao.getUserId()).isEqualTo(1); //verify DB records
        assertThat(taskFromDao.getTaskState()).isEqualTo(TaskState.TODO);
    }

    @Test
    void createNewTaskFailAuthenticationCase() throws Exception {

        //task with no user ID
        Task task = new Task(1L, null, "Task description 1.", LocalDate.now(), TaskState.TODO);

        //make a request
        mockMvc.perform(MockMvcRequestBuilders.post("/todo-service/v1/create-task")
                        .content(objectMapper.writeValueAsString(task))
                        .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden()) //verify response status
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Authentication problem.")));

        Task taskFromDao = taskDao.getOneTask(1);
        assertThat(taskFromDao).isNull(); //verify that it was not added to DB
    }

    @Test
    void createNewTaskFailsOnConflictCase() throws Exception {

        Task task = new Task(1L, 1L, "Task description 1.", LocalDate.now(), TaskState.TODO);

        //make a request
        mockMvc.perform(MockMvcRequestBuilders.post("/todo-service/v1/create-task")
                .content(objectMapper.writeValueAsString(task))
                .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON));

        //new task identical to already existing one
        Task identicalTask = new Task(1L, 1L, "Task description 2.", LocalDate.now(), TaskState.TODO);

        mockMvc.perform(MockMvcRequestBuilders.post("/todo-service/v1/create-task")
                        .content(objectMapper.writeValueAsString(identicalTask))
                        .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isConflict()) //verify response status
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Task with this ID already exists.")));

        Task taskFromDao = taskDao.getOneTask(1);
        assertThat(taskFromDao.getUserId()).isEqualTo(1);
    }

    @Test
    void getListOfAllTasksSuccessfulCase() throws Exception {

        Task task1 = new Task(1L, 1L, "Task description 1.", LocalDate.now(), TaskState.TODO);
        Task task2 = new Task(3L, 1L, "Task description 2.", LocalDate.now(), TaskState.IN_PROGRESS);

        //create one task
        mockMvc.perform(MockMvcRequestBuilders.post("/todo-service/v1/create-task")
                        .content(objectMapper.writeValueAsString(task1))
                        .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated()); //verify response status

        //create second task
        mockMvc.perform(MockMvcRequestBuilders.post("/todo-service/v1/create-task")
                        .content(objectMapper.writeValueAsString(task2))
                        .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated()); //verify response status

        //verify a List of tasks
        mockMvc.perform(MockMvcRequestBuilders.get("/todo-service/v1/list")
                        .param("userId", "1")
                        .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()); //verify response status

        Map<Integer, Task> taskFromDao = taskDao.getAllTasks();
        assertThat(taskFromDao.size()).isEqualTo(2);
    }

    @Test
    void updateNewTaskSuccessfulCase() throws Exception {

        createNewTaskSuccessfullyCreatedCase(); //pre-requisite

        //update task with new description
        Task updatedTask = new Task(1L, 1L, "New description.", LocalDate.now(), TaskState.TODO);

        //make a request
        mockMvc.perform(MockMvcRequestBuilders.put("/todo-service/v1/update-task")
                        .content(objectMapper.writeValueAsString(updatedTask))
                        .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()); //verify response status

        Task taskFromDao = taskDao.getOneTask(1);
        assertThat(taskFromDao.getDescription()).isEqualTo("New description."); //verify new description
    }

    @Test
    void updateNewTaskFailsOnNotFoundCase() throws Exception {
        //update non-existing task with new description
        Task updatedTask = new Task(12L, 1L, "New description.", LocalDate.now(), TaskState.TODO);

        //make a request
        mockMvc.perform(MockMvcRequestBuilders.put("/todo-service/v1/update-task")
                        .content(objectMapper.writeValueAsString(updatedTask))
                        .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound()) //verify response status
                .andExpect(content().string(org.hamcrest.Matchers.containsString("This task can't be found.")));

        Task taskFromDao = taskDao.getOneTask(12);
        assertThat(taskFromDao).isNull(); //verify that it was not added to DB
    }

    @Test
    void deleteNewTaskSuccessfulCase() throws Exception {
        createNewTaskSuccessfullyCreatedCase(); //pre-requisite

        //make a request
        mockMvc.perform(MockMvcRequestBuilders.delete("/todo-service/v1/remove-task")
                        .param("userId", "1")
                        .param("taskId", "1")
                        .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()) //verify response status
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Task was successfully removed.")));

        Task taskFromDao = taskDao.getOneTask(1);
        assertThat(taskFromDao).isNull(); //verify it's deleted
    }

    @Test
    void deleteTaskFailsOnNotFoundCase() throws Exception {
        //verify that this task doesn't exist in DB
        taskDao.deleteTask(Math.toIntExact(1));

        //make a request
        mockMvc.perform(MockMvcRequestBuilders.delete("/todo-service/v1/remove-task")
                        .param("userId", "1")
                        .param("taskId", "1")
                        .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound()) //verify response status
                .andExpect(content().string(org.hamcrest.Matchers.containsString("This task can't be found.")));

        Task taskFromDao = taskDao.getOneTask(1);
        assertThat(taskFromDao).isNull(); //verify it's deleted
    }

}
