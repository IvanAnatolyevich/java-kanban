package server;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import main.controllers.Managers;
import main.controllers.TaskManager;
import main.model.Epic;
import main.model.Subtask;
import main.model.Task;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.regex.Pattern;

public class HttpTaskServer {
    public static final int PORT = 8080;

    private HttpServer server;
    private Gson gson;
    private TaskManager taskManager;

    public HttpTaskServer(TaskManager taskManager) throws IOException {
        this.taskManager = taskManager;
        gson = Managers.getGson();
        server = HttpServer.create(new InetSocketAddress("localhost", PORT), 0);
        server.createContext("/tasks", this::handleTasks);
        server.createContext("/epics", this::handleEpics);
        server.createContext("/subtasks", this::handleSubtasks);
        server.createContext("/history", this::handleHistoryManager);
        server.createContext("/prioritized", this::handlePrioritized);
    }

    public static void main(String[] args) {
    }

    private void handleTasks(HttpExchange httpExchange) throws IOException {
        try {
            String path = httpExchange.getRequestURI().getPath();
        String requestMethod = httpExchange.getRequestMethod();
        switch (requestMethod) {
            case "GET": {
                if (Pattern.matches("^/tasks$", path)) {
                    String response = gson.toJson(taskManager.getTasks());
                    sendText(httpExchange, response);
                    return;
                }
                if (Pattern.matches("^/tasks/\\d+$", path)) {
                    String pathId = path.replaceFirst("/tasks/", "");
                    int id = parsePathId(pathId);
                    if (id != -1) {
                        if (!taskManager.getTasks().contains(taskManager.getTask(id))) {
                            System.out.println("Задачи с id = " + id + " - не существует");
                            httpExchange.sendResponseHeaders(404, 0);
                            break;
                        }
                        String response = gson.toJson(taskManager.getTask(id));
                        sendText(httpExchange, response);
                        break;
                    } else {
                        System.out.println("Получен некорректный id = " + pathId);
                        httpExchange.sendResponseHeaders(404, 0);
                        break;
                    }
                }
            }
            break;   // Здесь надо быть внимательней
            case "DELETE": {
                if (Pattern.matches("^/tasks/\\d+$", path)) {
                    String pathId = path.replaceFirst("/tasks/", "");
                    int id = parsePathId(pathId);
                    if (id != -1) {
                        taskManager.deleteTask(id);
                        System.out.println("Удалили задачу id = " + id);
                        httpExchange.sendResponseHeaders(200, 0);
                    } else {
                        System.out.println("Получен некорректный id = " + pathId);
                        httpExchange.sendResponseHeaders(404, 0);

                    }
                } else {
                System.out.println("Получен некорректный path = " + path);
                httpExchange.sendResponseHeaders(404, 0);
                }
            }
            break;
            case "POST": {
                if (Pattern.matches("^/tasks$", path)) {
                    String body = readText(httpExchange);
                    if (body.isBlank()) {
                        httpExchange.sendResponseHeaders(400, 0);
                        break;
                    }
                    Task task = gson.fromJson(body, Task.class);
                    taskManager.addTask(task);
                    if (!taskManager.getTasks().contains(task)) {
                        httpExchange.sendResponseHeaders(406, 0);
                        break;
                    }
                    System.out.println("Задача добавлена");
                    httpExchange.sendResponseHeaders(201, 0);
                }
            }
            break;
            default: {
                System.out.println("Ждем GET, POST или DELETE запрос, а получили - " + requestMethod);
                httpExchange.sendResponseHeaders(404, 0);
            }
        }

        } catch (Exception exception) {
            exception.printStackTrace();
        } finally {
            httpExchange.close();
        }
    }

    private void handleEpics(HttpExchange httpExchange) throws IOException {
        try {
            String path = httpExchange.getRequestURI().getPath();
            String requestMethod = httpExchange.getRequestMethod();
            switch (requestMethod) {
                case "GET": {
                    if (Pattern.matches("^/epics$", path)) {
                        String response = gson.toJson(taskManager.getEpics());
                        sendText(httpExchange, response);
                        return;
                    }
                    if (Pattern.matches("^/epics/\\d+$", path)) {
                        String pathId = path.replaceFirst("/epics/", "");
                        int id = parsePathId(pathId);
                        if (id != -1) {
                            if (!taskManager.getEpics().contains(taskManager.getEpic(id))) {
                                System.out.println("Задачи с id = " + id + " - не существует");
                                httpExchange.sendResponseHeaders(404, 0);
                                break;
                            }
                            String response = gson.toJson(taskManager.getEpic(id));
                            sendText(httpExchange, response);
                            break;
                        } else {
                            System.out.println("Получен некорректный id = " + pathId);
                            httpExchange.sendResponseHeaders(404, 0);
                            break;
                        }
                    }
                }
                break;
                case "DELETE": {
                    if (Pattern.matches("^/epics/\\d+$", path)) {
                        String pathId = path.replaceFirst("/epics/", "");
                        int id = parsePathId(pathId);
                        if (id != -1) {
                            taskManager.deleteEpic(id);
                            System.out.println("Удалили эпик id = " + id);
                            httpExchange.sendResponseHeaders(200, 0);
                        } else {
                            System.out.println("Получен некорректный id = " + pathId);
                            httpExchange.sendResponseHeaders(404, 0);

                        }
                    } else {
                        System.out.println("Получен некорректный path = " + path);
                        httpExchange.sendResponseHeaders(404, 0);
                    }
                }
                break;
                case "POST": {
                    if (Pattern.matches("^/epics$", path)) {
                        String body = readText(httpExchange);
                        if (body.isBlank()) {
                            httpExchange.sendResponseHeaders(400, 0);
                            break;
                        }
                        Epic epic = gson.fromJson(body, Epic.class);
                        taskManager.addEpic(epic);
                        if (!taskManager.getTasks().contains(epic)) {
                            httpExchange.sendResponseHeaders(406, 0);
                            break;
                        }
                        System.out.println("Эпик добавлена");
                        httpExchange.sendResponseHeaders(201, 0);
                    }
                }
                break;
                default: {
                    System.out.println("Ждем GET, POST или DELETE запрос, а получили - " + requestMethod);
                    httpExchange.sendResponseHeaders(404, 0);
                }
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        } finally {
            httpExchange.close();
        }
    }

    private void handleSubtasks(HttpExchange httpExchange) throws IOException {
        try {
            String path = httpExchange.getRequestURI().getPath();
            String requestMethod = httpExchange.getRequestMethod();
            switch (requestMethod) {
                case "GET": {
                    if (Pattern.matches("^/subtasks$", path)) {
                        String response = gson.toJson(taskManager.getSubtasks());
                        sendText(httpExchange, response);
                        return;
                    }
                    if (Pattern.matches("^/subtasks/\\d+$", path)) {
                        String pathId = path.replaceFirst("/subtasks/", "");
                        int id = parsePathId(pathId);
                        if (id != -1) {
                            if (!taskManager.getSubtasks().contains(taskManager.getSubtask(id))) {
                                System.out.println("Задачи с id = " + id + " - не существует");
                                httpExchange.sendResponseHeaders(404, 0);
                                break;
                            }
                            String response = gson.toJson(taskManager.getSubtask(id));
                            sendText(httpExchange, response);
                            break;
                        } else {
                            System.out.println("Получен некорректный id = " + pathId);
                            httpExchange.sendResponseHeaders(404, 0);
                            break;
                        }
                    }
                }
                break;
                case "DELETE": {
                    if (Pattern.matches("^/subtasks/\\d+$", path)) {
                        String pathId = path.replaceFirst("/subtasks/", "");
                        int id = parsePathId(pathId);
                        if (id != -1) {
                            taskManager.deleteSubtask(id);
                            System.out.println("Удалили подзадачу id = " + id);
                            httpExchange.sendResponseHeaders(200, 0);
                        } else {
                            System.out.println("Получен некорректный id = " + pathId);
                            httpExchange.sendResponseHeaders(404, 0);

                        }
                    } else {
                        System.out.println("Получен некорректный path = " + path);
                        httpExchange.sendResponseHeaders(404, 0);
                    }
                }
                break;
                case "POST": {
                    if (Pattern.matches("^/subtasks/\\d+$", path)) {
                        String pathId = path.replaceFirst("/subtasks/", "");
                        int id = parsePathId(pathId);
                        if (id != -1) {
                            String body = readText(httpExchange);
                            if (body.isBlank()) {
                                httpExchange.sendResponseHeaders(400, 0);
                                break;
                            }
                            Subtask subtask = gson.fromJson(body, Subtask.class);
                            taskManager.addSubtask(subtask, id);
                            if (!taskManager.getEpics().contains(id)) {
                                httpExchange.sendResponseHeaders(404, 0);
                                break;
                            }
                            if (!taskManager.getTasks().contains(subtask)) {
                                httpExchange.sendResponseHeaders(406, 0);
                                break;
                            }
                            System.out.println("Задача добавлена");
                            httpExchange.sendResponseHeaders(201, 0);
                        }
                    }
                }
                break;
                default: {
                System.out.println("Ждем GET, POST или DELETE запрос, а получили - " + requestMethod);
                httpExchange.sendResponseHeaders(404, 0);
                }
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        } finally {
            httpExchange.close();
        }
    }

    private void handleHistoryManager(HttpExchange httpExchange) throws IOException {
        try {
            String path = httpExchange.getRequestURI().getPath();
            String requestMethod = httpExchange.getRequestMethod();
            if (requestMethod.equals("GET")) {
                if (Pattern.matches("^/history$", path)) {
                    String str = path.replaceFirst("/history", "");
                    if (str.isEmpty()) {
                        String response = gson.toJson(taskManager.getHistory());
                        sendText(httpExchange, response);
                    } else {
                        httpExchange.sendResponseHeaders(404, 0);
                    }
                }
            } else {
                System.out.println("Ждем GET запрос, а получили - " + requestMethod);
                httpExchange.sendResponseHeaders(404, 0);
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        } finally {
            httpExchange.close();
        }
    }

    private void handlePrioritized(HttpExchange httpExchange) throws IOException {
        try {
            String path = httpExchange.getRequestURI().getPath();
            String requestMethod = httpExchange.getRequestMethod();
            if (requestMethod.equals("GET")) {
                if (Pattern.matches("^/prioritized$", path)) {
                    String response = gson.toJson(taskManager.getPrioritized());
                    sendText(httpExchange, response);
                }
            } else {
                System.out.println("Ждем GET запрос, а получили - " + requestMethod);
                httpExchange.sendResponseHeaders(404, 0);
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        } finally {
            httpExchange.close();
        }
    }

    public Gson getGson() {
        return gson;
    }

    public TaskManager getTaskManager() {
        return taskManager;
    }


    private int parsePathId(String path) {
        try {
            return  Integer.parseInt(path);
        } catch (NumberFormatException exception) {
            return -1;
        }
    }

    public void start() {
        System.out.println("Started UserServer " + PORT);
        System.out.println("http://localhost:" + PORT + "/");
        server.start();
    }

    public void stop() {
        server.stop(0);
        System.out.println("Остановили сервер на порту " + PORT);
    }

    private String readText(HttpExchange h) throws IOException {
        return new String(h.getRequestBody().readAllBytes(),StandardCharsets.UTF_8);
    }

    private void sendText(HttpExchange h, String text) throws IOException {
        byte[] resp = text.getBytes(StandardCharsets.UTF_8);
        h.getResponseHeaders().add("Content-Type", "application/json;charset=utf-8");
        h.sendResponseHeaders(200, resp.length);
        h.getResponseBody().write(resp);
        h.close();
    }
}
