package api;

import com.google.gson.JsonSyntaxException;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import managers.TaskManager;
import exeptions.NotFoundException;
import exeptions.TaskOverlapException;
import tasks.Epic;
import tasks.Subtask;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class EpicHandler extends BaseHttpHandler implements HttpHandler {
    private final TaskManager taskManager;

    public EpicHandler(TaskManager taskManager) {
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
                        handleGetAllEpics(exchange);
                    } else if (pathParts.length == 3) {
                        handleGetEpicById(exchange, Integer.parseInt(pathParts[2]));
                    } else if (pathParts.length == 4 && "subtasks".equals(pathParts[3])) {
                        handleGetEpicSubtasks(exchange, Integer.parseInt(pathParts[2]));
                    }
                    break;
                case "POST":
                    handleCreateOrUpdateEpic(exchange);
                    break;
                case "DELETE":
                    if (pathParts.length == 3) {
                        handleDeleteEpic(exchange, Integer.parseInt(pathParts[2]));
                    }
                    break;
                default:
                    sendNotFound(exchange, "Method not allowed");
            }
        } catch (NumberFormatException e) {
            sendBadRequest(exchange, "Invalid epic id format");
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

    private void handleGetAllEpics(HttpExchange exchange) throws IOException {
        String response = gson.toJson(taskManager.getEpicList());
        sendText(exchange, response);
    }

    private void handleGetEpicById(HttpExchange exchange, int id) throws IOException {
        Epic epic = taskManager.getEpicById(id);
        sendText(exchange, gson.toJson(epic));
    }

    private void handleGetEpicSubtasks(HttpExchange exchange, int epicId) throws IOException {
        Epic epic = taskManager.getEpicById(epicId);
        List<Subtask> subtasks = taskManager.getEpicsSubtasks(epic);
        sendText(exchange, gson.toJson(subtasks));
    }

    private void handleCreateOrUpdateEpic(HttpExchange exchange) throws IOException {
        InputStream inputStream = exchange.getRequestBody();
        String body = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
        Epic epic = gson.fromJson(body, Epic.class);

        if (epic.getId() == 0) {
            taskManager.addEpic(epic);
            sendText(exchange, gson.toJson(epic), 201);
        } else {
            taskManager.updateEpic(epic);
            sendText(exchange, gson.toJson(epic));
        }
    }

    private void handleDeleteEpic(HttpExchange exchange, int id) throws IOException {
        taskManager.deleteEpicById(id);
    }
}