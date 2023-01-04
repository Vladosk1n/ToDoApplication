# ToDoApplication
Application to manage TODO task list.

## Description
The TODO Application handles 4 different types of operations on Tasks:
- Creating new todo task
- Updating existing one
- Reviewing the list of all tasks
- Deleting specific task

The application accepts these four REST API requests and after validating requests 
and doing DB operation returns a response to the client.
Application does not use any user auth and only validates user auth based 
on userId being included within every request. For some requests 
it as included as param, for some it is in the body of request (see details in documentation section).

## Installation
In order to start the server and run it you need:
- Java 17
- Redis database
- cURL for testing (also Postman can be used)

Checkout the project from the GitHub link.
Make sure you have jdk 17 installed and set as the project and compile language.
JDK can be downloaded from: https://openjdk.org/projects/jdk/17/

This application runs on default Spring Boot server on your local machine so there should be no need to adjust anything to the Application Runner configuration.

For installing Redis please follow: https://redis.io/docs/getting-started/

Once Redis is successfully installed make sure you start it with 'redis-server
' command from cmd. Verify that you see some Redis logs. Redis uses default settings so there is no specific
yaml configuration for it.

To download cURL client (may not be needed for some systems) visit: https://idratherbewriting.com/learnapidoc/docapis_install_curl.html

## Documentation
Documentation on each request is available in task-openapi-specification.yaml file. Use it along with swagger: https://editor.swagger.io/
## Testing
Once you have application up and running, it's time to test it and verify that it indeed works.
Examples to run with one client:

POST request to create a new task:
```
curl --location --request POST 'localhost:8080/todo-service/v1/create-task' \
--header 'Content-Type: application/json' \
--data-raw '{
    "taskId": "1",
    "userId": "1",
    "description": "my test task 1",
    "deadline": "2022-12-30",
    "taskState": "IN_PROGRESS"
}'
```

PUT request to update an existing task:
```
curl --location --request PUT 'localhost:8080/todo-service/v1/update-task' \
--header 'Content-Type: application/json' \
--data-raw '{
    "taskId": "1",
    "userId": "1",
    "description": "new UPDATED description",
    "deadline": "2022-12-30",
    "taskState": "IN_PROGRESS"
}'
```
GET requests to retrieve all tasks:
```
curl --location --request GET 'localhost:8080/todo-service/v1/list?userId=1' \
--data-raw ''
```

Delete request to remove an existing task:
```
curl --location --request DELETE 'localhost:8080/todo-service/v1/remove-task?userId=1&taskId=1' \
--data-raw ''
```
Data is live in Redis so even after stopping and restarting server it should stay there.


In order to test server and requests coming from two clients run following:
```
curl --location --request POST 'localhost:8080/todo-service/v1/create-task' \
--header 'Content-Type: application/json' \
--data-raw '{
    "taskId": "1",
    "userId": "1",
    "description": "my test task 1",
    "deadline": "2022-12-30",
    "taskState": "IN_PROGRESS"
}' & curl --location --request POST 'localhost:8080/todo-service/v1/create-task' \
--header 'Content-Type: application/json' \
--data-raw '{
    "taskId": "2",
    "userId": "1",
    "description": "my test task 2",
    "deadline": "2022-12-30",
    "taskState": "IN_PROGRESS"
}' 
```
This will run multiple curl requests asynchronously.