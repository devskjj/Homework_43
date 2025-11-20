package Server;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public class Utility {
    public static void initRoutes(HttpServer server) {

        server.createContext("/", exchange -> showRoute(exchange, "Это корневой путь."));
        server.createContext("/apps/", exchange -> showRoute(exchange, "Это путь приложения."));
        server.createContext("/apps/profile", exchange -> showRoute(exchange, "Это путь профиля."));
    }

    private static void sendResponse(HttpExchange exchange, int statusCode, String msg) throws IOException {
        byte[] bytes = msg.getBytes(StandardCharsets.UTF_8);

        exchange.getResponseHeaders().add("Content-Type", "text/plain; charset=UTF-8");
        exchange.sendResponseHeaders(statusCode, bytes.length);

        try (OutputStream os = exchange.getResponseBody()) {
            os.write(bytes);
        }
    }



    private static void showRoute(HttpExchange exchange, String msg) throws IOException {
        sendResponse(exchange, 200, msg);
    }



    private static void writeFile(HttpExchange exchange, Path filePath, String type) throws IOException {
        byte[] fileBytes = Files.readAllBytes(filePath);
        exchange.getResponseHeaders().set("Content-Type", type);
        exchange.sendResponseHeaders(200, fileBytes.length);

        try (OutputStream os = exchange.getResponseBody()) {
            os.write(fileBytes);
        }
    }

    private static Path getRequestUrlPath(HttpExchange exchange) {
        String uri = exchange.getRequestURI().getPath();
        if (uri.startsWith("/")) {
            uri = uri.substring(1);
        }
        return Path.of("src/Data", uri);
    }
}