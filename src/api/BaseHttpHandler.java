package api;

import api.adapters.GsonUtils;
import com.google.gson.*;
import com.sun.net.httpserver.HttpExchange;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class BaseHttpHandler {
    protected final Gson gson = GsonUtils.createGson();

    protected void sendBadRequest(HttpExchange exchange, String message) throws IOException {
        sendText(exchange, message, 400);
    }

    protected void sendText(HttpExchange exchange, String text, int statusCode) throws IOException {
        byte[] resp = text.getBytes(StandardCharsets.UTF_8);
        exchange.getResponseHeaders().add("Content-Type", "application/json;charset=utf-8");
        exchange.sendResponseHeaders(statusCode, resp.length);
        exchange.getResponseBody().write(resp);
        exchange.close();
    }

    protected void sendText(HttpExchange exchange, String text) throws IOException {
        sendText(exchange, text, 200);
    }

    protected void sendNotFound(HttpExchange exchange, String message) throws IOException {
        sendText(exchange, message, 404);
    }

    protected void sendHasInteractions(HttpExchange exchange, String message) throws IOException {
        sendText(exchange, message, 406);
    }

    protected void sendInternalError(HttpExchange exchange, String message) throws IOException {
        sendText(exchange, message, 500);
    }
}
