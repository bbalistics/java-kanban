package http.tests;

import api.HttpTaskServer;
import api.adapters.GsonUtils;
import com.google.gson.Gson;
import managers.InMemoryTaskManager;
import managers.TaskManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;
import enums.Status;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class HttpEndpointsTest {
    private TaskManager manager;
    private HttpTaskServer taskServer;
    private Gson gson;
    private final HttpClient client = HttpClient.newHttpClient();

    @BeforeEach
    public void setUp() throws IOException {
        manager = new InMemoryTaskManager();
        taskServer = new HttpTaskServer(manager);
        gson = GsonUtils.createGson();
        taskServer.start();
    }

    @AfterEach
    public void shutDown() {
        taskServer.stop();
    }

    @Test
    public void testTaskEndpoints() throws IOException, InterruptedException {
        Task task = new Task("Test Task", "Description", 0, Status.NEW,
                LocalDateTime.now(), Duration.ofMinutes(30));
        String taskJson = gson.toJson(task);

        HttpRequest createRequest = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/tasks"))
                .POST(HttpRequest.BodyPublishers.ofString(taskJson))
                .build();

        HttpResponse<String> createResponse = client.send(createRequest, HttpResponse.BodyHandlers.ofString());
        assertEquals(201, createResponse.statusCode());
    }

    @Test
    public void testEpicEndpoints() throws IOException, InterruptedException {
        Epic epic = new Epic("Test Epic", "Description");
        String epicJson = gson.toJson(epic);

        HttpRequest createRequest = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/epics"))
                .POST(HttpRequest.BodyPublishers.ofString(epicJson))
                .build();

        HttpResponse<String> createResponse = client.send(createRequest, HttpResponse.BodyHandlers.ofString());
        assertEquals(201, createResponse.statusCode());

        HttpRequest getSubtasksRequest = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/epics/1/subtasks"))
                .GET()
                .build();

        HttpResponse<String> getSubtasksResponse = client.send(getSubtasksRequest, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, getSubtasksResponse.statusCode());
    }

    @Test
    public void testSubtaskEndpoints() throws IOException, InterruptedException {
        Epic epic = new Epic("Epic", "Description");
        manager.addEpic(epic);

        Subtask subtask = new Subtask("Test Subtask", "Description", 1);
        String subtaskJson = gson.toJson(subtask);

        HttpRequest createRequest = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/subtasks"))
                .POST(HttpRequest.BodyPublishers.ofString(subtaskJson))
                .build();

        HttpResponse<String> createResponse = client.send(createRequest, HttpResponse.BodyHandlers.ofString());
        assertEquals(201, createResponse.statusCode());
    }

    @Test
    public void testHistoryEndpoint() throws IOException, InterruptedException {
        Task task = new Task("History Test", "Description");
        manager.addTask(task);
        manager.getTaskById(1);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/history"))
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        assertTrue(response.body().contains("History Test"));
    }

    @Test
    public void testPrioritizedEndpoint() throws IOException, InterruptedException {
        Task task = new Task("Priority Test", "Description", 0, Status.NEW,
                LocalDateTime.now(), Duration.ofMinutes(30));
        manager.addTask(task);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/prioritized"))
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        assertTrue(response.body().contains("Priority Test"));
    }

    @Test
    public void testNotFoundCases() throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/tasks/999"))
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(404, response.statusCode());
    }
}
