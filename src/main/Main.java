package main;
import main.controllers.Managers;
import main.controllers.TaskManager;
import main.model.*;
import main.controllers.InMemoryTaskManager;
import main.util.Status;

public class Main {

    public static void main(String[] args) {
//        TaskManager taskManager = Managers.getDefaultTaskManager();
//        Task task = new Task("Задача1", "Описание1", Status.NEW);
//        Epic epic = new Epic("Задача2", "Описание2", Status.NEW);
//        taskManager.addTask(task);
//        taskManager.addEpic(epic);
//        taskManager.getTask(task.getId());
//        taskManager.getEpic(epic.getId());
//        System.out.println(taskManager.getHistory().get(0).getId());
//        System.out.println(taskManager.getHistory().get(1).getId());
//        task.setId(4);
//        task.setStatus(Status.IN_PROGRESS);
//        epic.setId(7);
//        epic.setStatus(Status.IN_PROGRESS);
//        System.out.println(taskManager.getHistory().get(0).getId());
//        System.out.println(taskManager.getHistory().get(1).getId());
//        Task task1 = new Task("Задача2", "Описание2");
//        TaskManager taskManager = Managers.getDefaultTaskManager();
//        taskManager.addTask(task1);
//        System.out.println(task1.getId());
//        taskManager.addTask(task);
//        System.out.println(task.getId());

//        Task task1 = new Task("Сдать ТЗ №4", "Создать логику приложения", Status.NEW);
//        Task task2 = new Task("Сдать ТЗ №5", "Провести тесты", Status.NEW);
//        Epic epic1 = new Epic("Освоить профессию программиста", "Научиться писать профессиональный код", Status.NEW);
//        Subtask subtask1 = new Subtask("Пройти курс от Яндекса", "Каждый день заниматься по 3-4 часа программированием", Status.NEW);
//        Subtask subtask2 = new Subtask("Прочитать книгу 'java: руководство для начинающих'", "Необходимо читать каждый день", Status.NEW);
//        Epic epic2 = new Epic("Важный эпик", "описание важного эпика", Status.NEW);
//        Subtask subtask3 = new Subtask("подзадача", "описание подзадачи", Status.NEW);
//        InMemoryTaskManager manager = (InMemoryTaskManager) Managers.getDefaultTaskManager();
//        manager.addTask(task1);
//        manager.addTask(task2);
//        manager.addEpic(epic1);
//        manager.addSubtask(subtask1, 2);
//        manager.addSubtask(subtask2, 2);
//        manager.addEpic(epic2);
//        manager.addSubtask(subtask3, 3);

//        System.out.println(manager.getTasks());
//        System.out.println(manager.getEpics());
//        System.out.println(manager.getSubtasks());
//
//        task1.setStatus(Status.IN_PROGRESS);
//        task2.setStatus(Status.IN_PROGRESS);
//        subtask1.setStatus(Status.IN_PROGRESS);
//        subtask2.setStatus(Status.IN_PROGRESS);
//        subtask3.setStatus(Status.IN_PROGRESS);
//
//        System.out.println(epic1.getStatus() + ", " + epic2.getStatus());
//        manager.updatedSubtask(subtask1);
//        manager.updatedSubtask(subtask2);
//        manager.updatedSubtask(subtask3);
//        System.out.println(epic1.getStatus() + ", " + epic2.getStatus());
//
//        System.out.println(epic2.getSubtasks());
//
//        manager.deleteEpic(2);
//        manager.deleteSubtask(6);
//        System.out.println(epic2.getSubtasks());
//
//
//        System.out.println(manager.getTasks());
//        System.out.println(manager.getEpics());
//        System.out.println(manager.getSubtasks());





    }
}
