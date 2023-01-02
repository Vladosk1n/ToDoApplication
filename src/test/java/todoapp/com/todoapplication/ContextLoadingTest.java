package todoapp.com.todoapplication;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import todoapp.com.todoapplication.controller.TaskController;

@SpringBootTest
class ContextLoadingTest {

    @Autowired
    private TaskController controller;

    @Test
    void contextLoads() {
        //to test that context is creating our controller
        assertThat(controller).isNotNull();
    }

}
