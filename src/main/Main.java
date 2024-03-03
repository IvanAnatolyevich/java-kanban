package main;
import main.model.*;
import main.controllers.TaskManager;
import main.util.Status;

public class Main {

    public static void main(String[] args) {

        Task task1 = new Task("Сдать ТЗ №4", "Создать логику приложения");
        Task task2 = new Task("Сдать ТЗ №5", "Провести тесты");
        Epic epic1 = new Epic("Освоить профессию программиста", "Научиться писать профессиональный код");
        Subtask subtask1 = new Subtask("Пройти курс от Яндекса", "Каждый день заниматься по 3-4 часа программированием");
        Subtask subtask2 = new Subtask("Прочитать книгу 'java: руководство для начинающих'", "Необходимо читать каждый день");
        Epic epic2 = new Epic("Важный эпик", "описание важного эпика");
        Subtask subtask3 = new Subtask("подзадача", "описание подзадачи");
        TaskManager manager = new TaskManager();
        manager.addTask(task1);
        manager.addTask(task2);
        manager.addEpic(epic1);
        manager.addSubtask(subtask1, 2);
        manager.addSubtask(subtask2, 2);
        manager.addEpic(epic2);
        manager.addSubtask(subtask3, 5);

        System.out.println(manager.getTasks());
        System.out.println(manager.getEpics());
        System.out.println(manager.getSubtasks());

        task1.setStatus(Status.IN_PROGRESS);
        task2.setStatus(Status.IN_PROGRESS);
        subtask1.setStatus(Status.IN_PROGRESS);
        subtask2.setStatus(Status.IN_PROGRESS);
        subtask3.setStatus(Status.IN_PROGRESS);

        System.out.println(epic1.getStatus() + ", " + epic2.getStatus());
        manager.updatedSubtask(subtask1);
        manager.updatedSubtask(subtask2);
        manager.updatedSubtask(subtask3);
        System.out.println(epic1.getStatus() + ", " + epic2.getStatus());

        System.out.println(epic2.getSubtasks());

        manager.deleteEpic(2);
        manager.deleteSubtask(6);
        System.out.println(epic2.getSubtasks());


        System.out.println(manager.getTasks());
        System.out.println(manager.getEpics());
        System.out.println(manager.getSubtasks());





    }
}
