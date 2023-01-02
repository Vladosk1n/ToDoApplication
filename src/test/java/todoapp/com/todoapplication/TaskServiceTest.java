package todoapp.com.todoapplication;

import com.fasterxml.jackson.databind.ObjectMapper;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.RequestEntity.post;
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

    @Test
    void createNewTasksSuccessfullyCreatedCase() throws Exception {

        Task task = new Task();
        task.setTaskId(1L);
        task.setTaskState(TaskState.TODO);
        task.setUserId(1L);
        task.setDescription("Task description 1.");

        task.setDeadline(LocalDate.now());

        taskDao.deleteTask(Math.toIntExact(task.getTaskId())); //remove task in case it already exists in redis

        //make a request
        mockMvc.perform(MockMvcRequestBuilders.post("/todo-service/v1/create-task")
                        .content(objectMapper.writeValueAsString(task))
                        .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated()); //verify response status

        Task taskFromDao = taskDao.getOneTask(1);

        assertThat(taskFromDao.getUserId()).isEqualTo(1); //verify DB records
        assertThat(taskFromDao.getTaskState()).isEqualTo(TaskState.TODO);

    }


}
