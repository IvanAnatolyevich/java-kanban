package main;
import main.controllers.*;
import main.model.*;
import main.util.Status;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) throws IOException, ManagerSaveException {
        TaskManager taskManager = Managers.getDefaultTaskManager();
        FileBackedTaskManager file = new FileBackedTaskManager(Managers.getDefaultHistoryManager());

        Task task1 = new Task("Задача1", "Описание1", Status.NEW);
        Epic epic = new Epic("Задача2", "Описание2", Status.NEW);
        Subtask subtask = new Subtask("Задача3", "Описание3", Status.NEW);


        file.addTask(task1);
        file.addEpic(epic);



//        Writer fileWriter = new FileWriter("File.SCV");
//        fileWriter.write(task1.toString() + "\n");
//        fileWriter.write(epic.toString() + "\n");
//        fileWriter.write(subtask.toString() + "\n");
//
//        fileWriter.close();



            List<String> lines =  Files.readAllLines(Paths.get("File.SCV"));
        System.out.println(Task.class.getSimpleName().equals("Task"));
        taskManager.removeTaskAll();

        FileBackedTaskManager o = FileBackedTaskManager.loadFromFile(Paths.get("File.SCV").toFile());

        System.out.println(o.getTasks());

        for (String s: lines) {
            System.out.println(s);
        }





        }
        }




