
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.awt.Desktop;
import java.io.*;
import java.net.InetSocketAddress;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

/**
 * Built-in zero-dependency HTTP server to serve the Smart Hospital Management System
 * Web Application on localhost.
 */
public class WebServer {

    private static final int PORT = 8080;
    private static final String WEB_DIR = "web";

    private static final Map<String, String> MIME_TYPES = new HashMap<>();

    static {
        MIME_TYPES.put("html", "text/html; charset=UTF-8");
        MIME_TYPES.put("htm", "text/html; charset=UTF-8");
        MIME_TYPES.put("css", "text/css; charset=UTF-8");
        MIME_TYPES.put("js", "application/javascript; charset=UTF-8");
        MIME_TYPES.put("json", "application/json; charset=UTF-8");
        MIME_TYPES.put("png", "image/png");
        MIME_TYPES.put("jpg", "image/jpeg");
        MIME_TYPES.put("jpeg", "image/jpeg");
        MIME_TYPES.put("svg", "image/svg+xml");
        MIME_TYPES.put("ico", "image/x-icon");
        MIME_TYPES.put("woff2", "font/woff2");
        MIME_TYPES.put("woff", "font/woff");
        MIME_TYPES.put("ttf", "font/ttf");
    }

    public static void main(String[] args) {
        int port = PORT;
        if (args.length > 0) {
            try {
                port = Integer.parseInt(args[0]);
            } catch (NumberFormatException ignored) {}
        }

        try {
            HttpServer server = HttpServer.create(new InetSocketAddress("localhost", port), 0);
            server.createContext("/", new StaticFileHandler());
            server.setExecutor(null); // Default single/multithread executor
            server.start();

            String url = "http://localhost:" + port;
            System.out.println("=================================================================");
            System.out.println("   SMART HOSPITAL MANAGEMENT SYSTEM - WEB APPLICATION SERVER");
            System.out.println("=================================================================");
            System.out.println(" [✔] Web Server is live at: " + url);
            System.out.println(" [✔] Serving web files from: ./" + WEB_DIR);
            System.out.println(" [ℹ] Press Ctrl+C in this console to stop the server.");
            System.out.println("=================================================================\n");

            // Open user's default browser automatically
            try {
                if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
                    Desktop.getDesktop().browse(new URI(url));
                }
            } catch (Exception e) {
                System.out.println("Please open your browser manually and visit: " + url);
            }

        } catch (IOException e) {
            System.err.println("Failed to start server on port " + port + ": " + e.getMessage());
            System.err.println("If port " + port + " is already in use, try running with another port: java WebServer.java 8081");
        }
    }

    static class StaticFileHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String pathStr = exchange.getRequestURI().getPath();
            if (pathStr.equals("/") || pathStr.isEmpty()) {
                pathStr = "/index.html";
            }

            // Security check against directory traversal
            if (pathStr.contains("..")) {
                sendResponse(exchange, 403, "text/plain", "Forbidden".getBytes());
                return;
            }

            Path filePath = Paths.get(WEB_DIR, pathStr.substring(1));
            File file = filePath.toFile();

            if (!file.exists() || file.isDirectory()) {
                // If requesting a path that doesn't exist, fallback to index.html for SPA routing
                file = Paths.get(WEB_DIR, "index.html").toFile();
            }

            if (!file.exists()) {
                String notFoundMsg = "404 Not Found: " + pathStr;
                sendResponse(exchange, 404, "text/plain", notFoundMsg.getBytes());
                return;
            }

            String ext = getFileExtension(file.getName());
            String contentType = MIME_TYPES.getOrDefault(ext, "application/octet-stream");

            byte[] bytes = Files.readAllBytes(file.toPath());
            sendResponse(exchange, 200, contentType, bytes);
        }

        private void sendResponse(HttpExchange exchange, int statusCode, String contentType, byte[] data) throws IOException {
            exchange.getResponseHeaders().set("Content-Type", contentType);
            exchange.getResponseHeaders().set("Access-Control-Allow-Origin", "*");
            exchange.getResponseHeaders().set("Cache-Control", "no-cache, no-store, must-revalidate");
            exchange.sendResponseHeaders(statusCode, data.length);
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(data);
            }
        }

        private String getFileExtension(String filename) {
            int lastDot = filename.lastIndexOf('.');
            if (lastDot > 0 && lastDot < filename.length() - 1) {
                return filename.substring(lastDot + 1).toLowerCase();
            }
            return "";
        }
    }
}
