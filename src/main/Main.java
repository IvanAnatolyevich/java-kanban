package main;
import main.controllers.*;
import main.model.*;
import main.util.Status;

public class Main {

    public static void main(String[] args) {
        TaskManager taskManager = Managers.getDefaultTaskManager();
        InMemoryHistoryManager historyManager = new InMemoryHistoryManager();
        Task task1 = new Task("Задача1", "Описание1", Status.NEW);
        Task task2 = new Task("Задача2", "Описание1", Status.NEW);
        Task task3 = new Task("Задача3", "Описание1", Status.NEW);
        Task task4 = new Task("Задача4", "Описание1", Status.NEW);
        Task task5 = new Task("Задача5", "Описание1", Status.NEW);

        taskManager.addTask(task1);
        taskManager.addTask(task2);
        taskManager.addTask(task3);
        taskManager.addTask(task4);
        taskManager.addTask(task5);

        taskManager.getTask(0);
        taskManager.getTask(1);
        taskManager.getTask(2);
        taskManager.getTask(3);
        taskManager.getTask(4);
        taskManager.getTask(0);
        taskManager.getTask(2);

        System.out.println(taskManager.getHistory());
    }
}
