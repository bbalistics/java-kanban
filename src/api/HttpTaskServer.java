package api;

import com.sun.net.httpserver.HttpServer;
import managers.Managers;
import managers.TaskManager;

import java.io.IOException;
import java.net.InetSocketAddress;

public class HttpTaskServer {
    private static final int PORT = 8080;
    private final HttpServer server;
    private final TaskManager taskManager;

    public HttpTaskServer(TaskManager taskManager) throws IOException {
        this.taskManager = taskManager;
        this.server = HttpServer.create(new InetSocketAddress(PORT), 0);
        this.server.createContext("/tasks", new TaskHandler(taskManager));
        this.server.createContext("/epics", new EpicHandler(taskManager));
        this.server.createContext("/subtasks", new SubtaskHandler(taskManager));
        this.server.createContext("/history", new HistoryHandler(taskManager));
        this.server.createContext("/prioritized", new PrioritizedHandler(taskManager));
    }

    public void start() {
        System.out.println("Сервер запущен на порту " + PORT);
        server.start();
    }

    public void stop() {
        server.stop(0);
        System.out.println("Сервер остановлен");
    }

    public static void main(String[] args) throws IOException {
        TaskManager manager = Managers.getDefault();
        HttpTaskServer taskServer = new HttpTaskServer(manager);
        taskServer.start();
    }
}
