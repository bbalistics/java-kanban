package api;

import com.google.gson.JsonSyntaxException;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import managers.TaskManager;
import exeptions.NotFoundException;
import exeptions.TaskOverlapException;
import tasks.Subtask;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class SubtaskHandler extends BaseHttpHandler implements HttpHandler {
    private final TaskManager taskManager;

    public SubtaskHandler(TaskManager taskManager) {
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
                        handleGetAllSubtasks(exchange);
                    } else if (pathParts.length == 3) {
                        handleGetSubtaskById(exchange, Integer.parseInt(pathParts[2]));
                    }
                    break;
                case "POST":
                    handleCreateOrUpdateSubtask(exchange);
                    break;
                case "DELETE":
                    if (pathParts.length == 3) {
                        handleDeleteSubtask(exchange, Integer.parseInt(pathParts[2]));
                    }
                    break;
                default:
                    sendNotFound(exchange, "Method not allowed");
            }
        } catch (NumberFormatException e) {
            sendBadRequest(exchange, "Invalid subtask id format");
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

    private void handleGetAllSubtasks(HttpExchange exchange) throws IOException {
        String response = gson.toJson(taskManager.getSubtasksList());
        sendText(exchange, response);
    }

    private void handleGetSubtaskById(HttpExchange exchange, int id) throws IOException {
        Subtask subtask = taskManager.getSubtaskById(id);
        sendText(exchange, gson.toJson(subtask));
    }

    private void handleCreateOrUpdateSubtask(HttpExchange exchange) throws IOException {
        InputStream inputStream = exchange.getRequestBody();
        String body = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
        Subtask subtask = gson.fromJson(body, Subtask.class);

        if (subtask.getId() == 0) {
            taskManager.addSubtask(subtask);
            sendText(exchange, gson.toJson(subtask), 201);
        } else {
            taskManager.updateSubtask(subtask);
            sendText(exchange, gson.toJson(subtask));
        }
    }

    private void handleDeleteSubtask(HttpExchange exchange, int id) throws IOException {
        taskManager.deleteSubtaskById(id);
    }
}
