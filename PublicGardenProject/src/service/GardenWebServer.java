package service;

import com.sun.net.httpserver.*;
import java.io.*;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import model.*;

public class GardenWebServer {

    private final Garden garden;
    private final RequestManager requestManager;
    private final String saveFile;
    private final int port;
    private HttpServer server;

    public GardenWebServer(Garden garden, RequestManager requestManager, String saveFile, int port) {
        this.garden = garden;
        this.requestManager = requestManager;
        this.saveFile = saveFile;
        this.port = port;
    }

    public void start() {
        try {
            server = HttpServer.create(new InetSocketAddress(port), 0);

            // 1. Serve frontend HTML page
            server.createContext("/", this::handleStatic);

            // 2. API to get all garden data (beds, plants, weather, requests)
            server.createContext("/api/garden", this::handleGardenData);

            // 3. API to submit a new plant request from the webpage
            server.createContext("/api/request", this::handleSubmitRequest);

            // 4. API for moderator to approve or reject a request
            server.createContext("/api/action", this::handleAction);

            server.setExecutor(null);
            server.start();

            System.out.println("🌱 Web App is live at: http://localhost:" + port);
        } catch (IOException e) {
            System.out.println("Could not start web server: " + e.getMessage());
        }
    }

    // Serve HTML, CSS, JS files from the "web/" folder
    private void handleStatic(HttpExchange exchange) throws IOException {
        String path = exchange.getRequestURI().getPath();
        if (path.equals("/") || path.isBlank()) {
            path = "/index.html";
        }

        File file = new File("web" + path);
        if (!file.exists() || file.isDirectory()) {
            String notFound = "<h1>404 Not Found</h1><p>Put your index.html in the web/ folder!</p>";
            byte[] bytes = notFound.getBytes(StandardCharsets.UTF_8);
            exchange.getResponseHeaders().set("Content-Type", "text/html");
            exchange.sendResponseHeaders(404, bytes.length);
            try (OutputStream os = exchange.getResponseBody()) { os.write(bytes); }
            return;
        }

        String mime = path.endsWith(".css") ? "text/css" :
                      path.endsWith(".js")  ? "application/javascript" : "text/html";

        byte[] bytes = Files.readAllBytes(file.toPath());
        exchange.getResponseHeaders().set("Content-Type", mime + "; charset=utf-8");
        exchange.sendResponseHeaders(200, bytes.length);
        try (OutputStream os = exchange.getResponseBody()) { os.write(bytes); }
    }

    // Return the Garden data as JSON so the browser can read it
    private void handleGardenData(HttpExchange exchange) throws IOException {
        if (!"GET".equalsIgnoreCase(exchange.getRequestMethod())) {
            exchange.sendResponseHeaders(405, -1);
            return;
        }

        StringBuilder json = new StringBuilder("{");
        json.append("\"welcomeMessage\":\"").append(escape(garden.getWelcomeMessage())).append("\",");
        json.append("\"location\":\"").append(escape(garden.getLocation())).append("\",");
        json.append("\"weather\":\"").append(escape(garden.getWeather())).append("\",");

        // Beds & Plants
        json.append("\"beds\":[");
        List<Bed> beds = garden.getBeds();
        for (int i = 0; i < beds.size(); i++) {
            Bed b = beds.get(i);
            json.append("{");
            json.append("\"index\":").append(i).append(",");
            json.append("\"capacity\":").append(b.getCapacity()).append(",");
            json.append("\"soil\":\"").append(escape(b.getSoil())).append("\",");
            json.append("\"plants\":[");
            List<Plant> plants = b.getPlants();
            if (plants != null) {
                for (int j = 0; j < plants.size(); j++) {
                    Plant p = plants.get(j);
                    String owner = (p.owner != null && p.owner.username != null) ? p.owner.username : "Anonymous";
                    json.append("{");
                    json.append("\"name\":\"").append(escape(p.name)).append("\",");
                    json.append("\"type\":\"").append(escape(p.type)).append("\",");
                    json.append("\"owner\":\"").append(escape(owner)).append("\"");
                    json.append("}");
                    if (j < plants.size() - 1) json.append(",");
                }
            }
            json.append("]}");
            if (i < beds.size() - 1) json.append(",");
        }
        json.append("],");

        // Pending Requests
        json.append("\"requests\":[");
        var requests = garden.getPlantQueue().requests;
        if (requests != null) {
            for (int i = 0; i < requests.size(); i++) {
                RequestForm r = requests.get(i);
                String user = (r.getUser() != null && r.getUser().username != null) ? r.getUser().username : "Anonymous";
                int bedIdx = garden.getBeds().indexOf(r.getBed());
                json.append("{");
                json.append("\"queueIndex\":").append(i).append(",");
                json.append("\"id\":").append(r.getRequestId()).append(",");
                json.append("\"plantName\":\"").append(escape(r.getPlantName())).append("\",");
                json.append("\"plantType\":\"").append(escape(r.getPlantType())).append("\",");
                json.append("\"bedIndex\":").append(bedIdx).append(",");
                json.append("\"user\":\"").append(escape(user)).append("\",");
                json.append("\"comment\":\"").append(escape(r.getComment())).append("\"");
                json.append("}");
                if (i < requests.size() - 1) json.append(",");
            }
        }
        json.append("]");

        json.append("}");

        byte[] bytes = json.toString().getBytes(StandardCharsets.UTF_8);
        exchange.getResponseHeaders().set("Content-Type", "application/json; charset=utf-8");
        exchange.getResponseHeaders().set("Access-Control-Allow-Origin", "*");
        exchange.sendResponseHeaders(200, bytes.length);
        try (OutputStream os = exchange.getResponseBody()) { os.write(bytes); }
    }

    // Submit a new request from the browser form
    private void handleSubmitRequest(HttpExchange exchange) throws IOException {
        if (!"POST".equalsIgnoreCase(exchange.getRequestMethod())) {
            exchange.sendResponseHeaders(405, -1);
            return;
        }

        String body = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
        String plantName = getParam(body, "plantName");
        String plantType = getParam(body, "plantType");
        String username  = getParam(body, "username");
        String comment   = getParam(body, "comment");
        int bedIndex     = 0;
        try { bedIndex = Integer.parseInt(getParam(body, "bedIndex")); } catch (Exception ignored) {}

        User user = null;
        for (User u : garden.getUsers()) {
            if (u.username != null && u.username.equalsIgnoreCase(username)) {
                user = u;
                break;
            }
        }
        if (user == null) {
            user = new Member();
            user.username = username.isBlank() ? "Guest" : username;
            garden.addUser(user);
        }

        if (bedIndex >= 0 && bedIndex < garden.getBeds().size()) {
            Bed bed = garden.getBeds().get(bedIndex);
            RequestForm form = new RequestForm(user, plantName, bed, comment, plantType);
            garden.getPlantQueue().requests.add(form);
            FileReader.saveGarden(garden, saveFile);
        }

        respondJson(exchange, "{\"success\":true}");
    }

    // Approve or reject a request from the browser
    private void handleAction(HttpExchange exchange) throws IOException {
        if (!"POST".equalsIgnoreCase(exchange.getRequestMethod())) {
            exchange.sendResponseHeaders(405, -1);
            return;
        }

        String body = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
        String action = getParam(body, "action");
        int index = 0;
        try { index = Integer.parseInt(getParam(body, "index")); } catch (Exception ignored) {}

        if ("approve".equalsIgnoreCase(action)) {
            requestManager.approveRequest(index);
            FileReader.saveGarden(garden, saveFile);
        } else if ("reject".equalsIgnoreCase(action)) {
            requestManager.rejectRequest(index);
            FileReader.saveGarden(garden, saveFile);
        }

        respondJson(exchange, "{\"success\":true}");
    }

    private void respondJson(HttpExchange exchange, String json) throws IOException {
        byte[] bytes = json.getBytes(StandardCharsets.UTF_8);
        exchange.getResponseHeaders().set("Content-Type", "application/json");
        exchange.sendResponseHeaders(200, bytes.length);
        try (OutputStream os = exchange.getResponseBody()) { os.write(bytes); }
    }

    private String escape(String s) {
        if (s == null) return "";
        return s.replace("\\", "\\\\").replace("\"", "\\\"").replace("\n", " ");
    }

    private String getParam(String body, String key) {
        for (String pair : body.split("&")) {
            String[] kv = pair.split("=");
            if (kv.length >= 2 && kv[0].equalsIgnoreCase(key)) {
                try {
                    return java.net.URLDecoder.decode(kv[1], StandardCharsets.UTF_8);
                } catch (Exception e) {
                    return kv[1];
                }
            }
        }
        return "";
    }
}