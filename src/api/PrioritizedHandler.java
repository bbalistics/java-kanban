package api;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import managers.TaskManager;
import exeptions.NotFoundException;
import tasks.Task;

import java.io.IOException;
import java.util.Set;

public class PrioritizedHandler extends BaseHttpHandler implements HttpHandler {
    private final TaskManager taskManager;

    public PrioritizedHandler(TaskManager taskManager) {
        this.taskManager = taskManager;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        try {
            if (!"GET".equalsIgnoreCase(exchange.getRequestMethod())) {
                sendNotFound(exchange, "Only GET method is allowed");
                return;
            }

            Set<Task> prioritizedTasks = taskManager.getPrioritizedTasks();
            String response = gson.toJson(prioritizedTasks);
            sendText(exchange, response);

        } catch (NotFoundException e) {
            sendNotFound(exchange, e.getMessage());
        } catch (Exception e) {
            sendInternalError(exchange, "Failed to get prioritized tasks: " + e.getMessage());
        }
    }
}
