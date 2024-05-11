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

public class HttpUserServer {
    public static final int PORT = 8080;

    private HttpServer server;
    private Gson gson;
    private TaskManager taskManager;

    public HttpUserServer(TaskManager taskManager) throws IOException {
        this.taskManager = taskManager;
        gson = Managers.getGson();
        server = HttpServer.create(new InetSocketAddress("localhost", PORT), 0);
        server.createContext("/", this::handleUsers);
    }


    private void handleUsers(HttpExchange httpExchange) {
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

                    if (Pattern.matches("^/api/subtasks$", path)) {
                        String response = gson.toJson(taskManager.getSubtasks());
                        sendText(httpExchange, response);
                        return;
                    } //2

                    if (Pattern.matches("^/epics$", path)) {
                        String response = gson.toJson(taskManager.getEpics());
                        sendText(httpExchange, response);
                        return;
                    }

                    if (Pattern.matches("^/prioritized$", path)) {
                        String response = gson.toJson(taskManager.getPrioritized());
                        sendText(httpExchange, response);
                        break;
                    }

                    if (Pattern.matches("^/history$", path)) {
                        String response = gson.toJson(taskManager.getHistory());
                        sendText(httpExchange, response);
                        break;
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
                            httpExchange.sendResponseHeaders(405, 0);
                            break;
                        }
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
                            httpExchange.sendResponseHeaders(405, 0);
                            break;
                        }
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
                            httpExchange.sendResponseHeaders(405, 0);
                            break;
                        }
                    }
                    break;
                }
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
                            httpExchange.sendResponseHeaders(405, 0);

                        }
                    } else {
                        System.out.println("Получен некорректный path = " + path);
                        httpExchange.sendResponseHeaders(405, 0);
                    }

                    if (Pattern.matches("^/subtasks/\\d+$", path)) {
                        String pathId = path.replaceFirst("/subtasks/", "");
                        int id = parsePathId(pathId);
                        if (id != -1) {
                            taskManager.deleteSubtask(id);
                            System.out.println("Удалили подзадачу id = " + id);
                            httpExchange.sendResponseHeaders(200, 0);
                        } else {
                            System.out.println("Получен некорректный id = " + pathId);
                            httpExchange.sendResponseHeaders(405, 0);

                        }
                    } else {
                        System.out.println("Получен некорректный path = " + path);
                        httpExchange.sendResponseHeaders(405, 0);
                    }

                    if (Pattern.matches("^/epics/\\d+$", path)) {
                        String pathId = path.replaceFirst("/epics/", "");
                        int id = parsePathId(pathId);
                        if (id != -1) {
                            taskManager.deleteEpic(id);
                            System.out.println("Удалили эпик id = " + id);
                            httpExchange.sendResponseHeaders(200, 0);
                        } else {
                            System.out.println("Получен некорректный id = " + pathId);
                            httpExchange.sendResponseHeaders(405, 0);

                        }
                    } else {
                        System.out.println("Получен некорректный path = " + path);
                        httpExchange.sendResponseHeaders(405, 0);
                    }
                    break;
                }
                case "POST": {
                    if (Pattern.matches("^/tasks$", path)) {
                        String body = readText(httpExchange);
                        Task task = gson.fromJson(body, Task.class);
                        taskManager.addTask(task);
                        if (!taskManager.getTasks().contains(task)) {
                            httpExchange.sendResponseHeaders(406, 0);
                            break;
                        }
                        System.out.println("Задача добавлена");
                        httpExchange.sendResponseHeaders(201, 0);
                    }

                    if (Pattern.matches("^/subtasks/\\d+$", path)) {
                        String pathId = path.replaceFirst("/subtasks/", "");
                        int id = parsePathId(pathId);
                        if (id != -1) {
                            String body = readText(httpExchange);
                            Subtask subtask = gson.fromJson(body, Subtask.class);
                            taskManager.addSubtask(subtask, id);
                            if (!taskManager.getEpics().contains(id)) {
                                httpExchange.sendResponseHeaders(405, 0);
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

                    if (Pattern.matches("^/epics$", path)) {
                        String body = readText(httpExchange);
                        Epic epic = gson.fromJson(body, Epic.class);
                        taskManager.addEpic(epic);
                        if (!taskManager.getTasks().contains(epic)) {
                            httpExchange.sendResponseHeaders(406, 0);
                            break;
                        }
                        System.out.println("Эпик добавлена");
                        httpExchange.sendResponseHeaders(201, 0);
                    }


                        break;
                }
                default: {
                    System.out.println("Ждем GET, POST или DELETE запрос, а получили - " + requestMethod);
                    httpExchange.sendResponseHeaders(405, 0);
                }
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
