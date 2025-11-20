package Server;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public class Utility {
    public static void initRoutes(HttpServer server) {
        server.createContext("/", exchange -> {
            String initPath = exchange.getRequestURI().getPath();
            if (initPath.contains(".")) {
                showFile(exchange);
            }

            if (initPath.equals("/") || initPath.startsWith("/") && !initPath.endsWith("/")) {
                showRoute(exchange, "Это корневой путь.");
            } else {
                showError(exchange, "Страница не найдена");
            }
        });
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

    private static void showError(HttpExchange exchange, String msg) throws IOException {
        sendResponse(exchange, 404, msg);
    }

    private static void showRoute(HttpExchange exchange, String msg) throws IOException {
        sendResponse(exchange, 200, msg);
    }

    private static Path getRequestUrlPath(HttpExchange exchange) {
        String uri = exchange.getRequestURI().getPath();
        if (uri.startsWith("/")) {
            uri = uri.substring(1);
        }
        return Path.of("src/Data", uri);
    }

    private static void showFile(HttpExchange exchange) throws IOException {
        Path filePath = getRequestUrlPath(exchange);
        if (Files.exists(filePath) && !Files.isDirectory(filePath)) {
            String type = Files.probeContentType(filePath);
            if (type == null) {
                type = "text/plain; charset=UTF-8";
            }
            writeFile(exchange, filePath, type);
        } else {
            showError(exchange, "Документ не найден");
        }
    }

    private static void writeFile(HttpExchange exchange, Path path, String type) throws IOException {
        byte[] fileBytes = Files.readAllBytes(path);
        exchange.getResponseHeaders().set("Content-Type", type);
        exchange.sendResponseHeaders(200, fileBytes.length);

        try (OutputStream os = exchange.getResponseBody()) {
            os.write(fileBytes);
        }
    }
}