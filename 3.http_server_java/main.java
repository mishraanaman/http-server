import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;

class Friend {
    int id;
    String name;

    Friend(int id, String name) {
        this.id = id;
        this.name = name;
    }
}

public class SimpleHttpServer {

    private static List<Friend> friends = new ArrayList<>();

    public static void main(String[] args) throws IOException {
        // Initialize sample data
        friends.add(new Friend(0, "Sir Isaac Newton"));
        friends.add(new Friend(2, "Sir Albert Einstein"));
        friends.add(new Friend(3, "Sir John Jacobs"));

        // Create an HTTP server
        HttpServer server = HttpServer.create(new InetSocketAddress(3000), 0);

        // Set up routes
        server.createContext("/friends", new FriendsHandler());
        server.createContext("/messages", new MessagesHandler());

        // Start the server
        server.setExecutor(null); // Use the default executor
        System.out.println("Server started on port 3000");
        server.start();
    }

    static class FriendsHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String method = exchange.getRequestMethod();
            String response = "";

            if ("GET".equalsIgnoreCase(method)) {
                String path = exchange.getRequestURI().getPath();
                String[] segments = path.split("/");

                if (segments.length == 3) {
                    // Fetch a specific friend by ID
                    try {
                        int id = Integer.parseInt(segments[2]);
                        Friend friend = friends.stream().filter(f -> f.id == id).findFirst().orElse(null);
                        if (friend != null) {
                            response = String.format("{\"id\": %d, \"name\": \"%s\"}", friend.id, friend.name);
                            exchange.getResponseHeaders().set("Content-Type", "application/json");
                            exchange.sendResponseHeaders(200, response.length());
                        } else {
                            response = "Friend not found.";
                            exchange.sendResponseHeaders(404, response.length());
                        }
                    } catch (NumberFormatException e) {
                        response = "Invalid ID.";
                        exchange.sendResponseHeaders(400, response.length());
                    }
                } else {
                    // Return all friends
                    StringBuilder jsonResponse = new StringBuilder("[");
                    for (int i = 0; i < friends.size(); i++) {
                        Friend friend = friends.get(i);
                        jsonResponse.append(String.format("{\"id\": %d, \"name\": \"%s\"}", friend.id, friend.name));
                        if (i < friends.size() - 1) {
                            jsonResponse.append(", ");
                        }
                    }
                    jsonResponse.append("]");
                    response = jsonResponse.toString();
                    exchange.getResponseHeaders().set("Content-Type", "application/json");
                    exchange.sendResponseHeaders(200, response.length());
                }
            } else if ("POST".equalsIgnoreCase(method)) {
                byte[] body = exchange.getRequestBody().readAllBytes();
                String requestBody = new String(body);
                try {
                    // Simulate parsing JSON
                    int id = friends.size(); // Assign the next ID automatically
                    String name = requestBody.replaceAll("\"", "").split(":")[1].trim();
                    friends.add(new Friend(id, name));
                    response = "Friend added successfully.";
                    exchange.sendResponseHeaders(201, response.length());
                } catch (Exception e) {
                    response = "Invalid request body.";
                    exchange.sendResponseHeaders(400, response.length());
                }
            } else {
                response = "Method not allowed.";
                exchange.sendResponseHeaders(405, response.length());
            }

            OutputStream os = exchange.getResponseBody();
            os.write(response.getBytes());
            os.close();
        }
    }

    static class MessagesHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            if ("GET".equalsIgnoreCase(exchange.getRequestMethod())) {
                String response = "<html><body>" +
                                  "<li>Hello Isaac</li>" +
                                  "<li>What are your thoughts on Astronomy?</li>" +
                                  "</body></html>";
                exchange.getResponseHeaders().set("Content-Type", "text/html");
                exchange.sendResponseHeaders(200, response.length());
                OutputStream os = exchange.getResponseBody();
                os.write(response.getBytes());
                os.close();
            } else {
                String response = "Method not allowed.";
                exchange.sendResponseHeaders(405, response.length());
                OutputStream os = exchange.getResponseBody();
                os.write(response.getBytes());
                os.close();
            }
        }
    }
}
