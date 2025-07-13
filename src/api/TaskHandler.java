package api;

import com.google.gson.JsonSyntaxException;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import managers.TaskManager;
import exeptions.NotFoundException;
import exeptions.TaskOverlapException;
import tasks.Task;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class TaskHandler extends BaseHttpHandler implements HttpHandler {
    private final TaskManager taskManager;

    public TaskHandler(TaskManager taskManager) {
        this.taskManager = taskManager;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        try {
            String method = exchange.getRequestMethod();
            String path = exchange.getRequestURI().getPath();
            String[] pathParts = path.split("/");

            switch (method) {
                case "GET":
                    if (pathParts.length == 2) {
                        handleGetAllTasks(exchange);
                    } else if (pathParts.length == 3) {
                        handleGetTaskById(exchange, Integer.parseInt(pathParts[2]));
                    }
                    break;
                case "POST":
                    handleCreateOrUpdateTask(exchange);
                    break;
                case "DELETE":
                    if (pathParts.length == 3) {
                        handleDeleteTask(exchange, Integer.parseInt(pathParts[2]));
                    }
                    break;
                default:
                    sendNotFound(exchange, "Method not allowed");
            }
        } catch (NumberFormatException e) {
            sendBadRequest(exchange, "Invalid task id format");
        } catch (NotFoundException e) {
            sendNotFound(exchange, e.getMessage());
        } catch (TaskOverlapException e) {
            sendHasInteractions(exchange, e.getMessage());
        } catch (JsonSyntaxException e) {
            sendBadRequest(exchange, "Invalid JSON format");
        } catch (Exception e) {
            sendInternalError(exchange, "Internal server error: " + e.getMessage());
        }
    }

    private void handleGetAllTasks(HttpExchange exchange) throws IOException {
        String response = gson.toJson(taskManager.getTaskList());
        sendText(exchange, response);
    }

    private void handleGetTaskById(HttpExchange exchange, int id) throws IOException {
        Task task = taskManager.getTaskById(id);
        sendText(exchange, gson.toJson(task));
    }

    private void handleCreateOrUpdateTask(HttpExchange exchange) throws IOException {
        InputStream inputStream = exchange.getRequestBody();
        String body = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
        Task task = gson.fromJson(body, Task.class);

        if (task.getId() == 0) {
            taskManager.addTask(task);
            sendText(exchange, gson.toJson(task), 201);
        } else {
            taskManager.updateTask(task);
            sendText(exchange, gson.toJson(task));
        }
    }

    private void handleDeleteTask(HttpExchange exchange, int id) throws IOException {
        taskManager.deleteTaskById(id);
    }
}