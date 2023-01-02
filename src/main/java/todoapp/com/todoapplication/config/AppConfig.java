package todoapp.com.todoapplication.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import todoapp.com.todoapplication.model.Task;

@Configuration
public class AppConfig {

    //Creating Connection with Redis
    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        return new LettuceConnectionFactory();
    }

    //Creating RedisTemplate for Entity 'Task'
    @Bean
    public RedisTemplate<String, Task> redisTemplate(){
        RedisTemplate<String, Task> taskTemplate = new RedisTemplate<>();
        taskTemplate.setConnectionFactory(redisConnectionFactory());
        return taskTemplate;
    }
}
