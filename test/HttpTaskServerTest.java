import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import main.controllers.Managers;
import main.model.Epic;
import main.model.Subtask;
import main.model.Task;
import main.util.Status;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import server.HttpTaskServer;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class HttpTaskServerTest {

    private HttpTaskServer userServer;
    private final Gson gson = Managers.getGson();
    private Task task;
    private Epic epic;
    private Subtask subtask;


    @BeforeEach
    void init() throws IOException {
        userServer = new HttpTaskServer(Managers.getDefaultTaskManager());

         task = new Task("Test task" , "Test task description",
                Status.NEW, Duration.ofMinutes(15),
                 LocalDateTime.of(2024, 4, 7,12,0));
         epic = new Epic("Test epic", "Test epic discription",
                 Status.NEW, Duration.ofMinutes(15),
                 LocalDateTime.of(2024,4, 7, 10,0));
         subtask = new Subtask("Test subtask", "Test subtask discription",
                 Status.NEW, Duration.ofMinutes(15),
                 LocalDateTime.of(2024,4,7,14,0));
        userServer.getTaskManager().addTask(task);
        userServer.getTaskManager().addEpic(epic);
        userServer.getTaskManager().addSubtask(subtask, 1);

        userServer.start();

    }

    @AfterEach
    void tearDown() {
        userServer.stop();
        userServer.getTaskManager().removeTaskAll();
        userServer.getTaskManager().removeEpicAll();
        userServer.getTaskManager().removeSubtaskAll();
    }

    @Test
    void getTasks() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI uri = URI.create("http://localhost:8080/tasks");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .version(HttpClient.Version.HTTP_1_1)
                .header("Accept", "application/json")
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());

        Type taskType = new TypeToken<ArrayList<Task>>() {}.getType();
        List<Task> actual = userServer.getGson().fromJson(response.body(), taskType);

        assertEquals(task, actual.get(0));
        assertEquals(1, actual.size());
    }

    @Test
    void deleteTasks() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI uri = URI.create("http://localhost:8080/tasks/0");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .DELETE()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());


    }

}