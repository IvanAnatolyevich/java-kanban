package main.controllers;

import main.exceptions.ManagerSaveException;
import main.model.Epic;
import main.model.Subtask;
import main.model.Task;

import java.io.*;
import java.nio.file.Files;
import java.util.List;

public class FileBackedTaskManager extends InMemoryTaskManager {
    public FileBackedTaskManager(HistoryManager historyManager) {
        super(historyManager);
    }

    public void save() {
        try (Writer fileWriter = new FileWriter("File.SCV")) {
            for (Task el: tasks.values()) {
                fileWriter.write(el.toString() + "\n");
            }
            for (Epic el: epics.values()) {
                fileWriter.write(el.toString() + "\n");
            }
            for (Subtask el: subtasks.values()) {
                fileWriter.write(el.toString() + "\n");
            }
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка сохранения файла");
        }
    }

    public static FileBackedTaskManager loadFromFile(File file) {
        FileBackedTaskManager fileBackedTaskManager = new FileBackedTaskManager(Managers.getDefaultHistoryManager());
       try {
           List<String> strings = Files.readAllLines(file.toPath());
           for (String el: strings) {
               String[] obj = el.split(",");
               if (obj[1].equals("Task")) {
                   Task task = Task.fromString(el);
                   tasks.put(task.getId(), task);
               } else if (obj[1].equals("Epic")) {
                   Epic epic = (Epic) Epic.fromString(el);
                   epics.put(epic.getId(), epic);
               } else if (obj[1].equals("Subtask")) {
                   Subtask subtask = (Subtask) Subtask.fromString(el);
                   subtasks.put(subtask.getId(), subtask);
               }
           }
           return fileBackedTaskManager;
       } catch (IOException e) {
           throw new RuntimeException(e);
       }
    }

    @Override
    public Task addTask(Task newTask) {
        Task task = super.addTask(newTask);
        save();
        return task;
    }

    @Override
    public Subtask addSubtask(Subtask newSuptask, int epicId) {
        Subtask subtask = super.addSubtask(newSuptask, epicId);
        save();
        return subtask;
    }

    @Override
    public Epic addEpic(Epic newEpic) {
        Epic epic = super.addEpic(newEpic);
        save();
        return epic;
    }

    @Override
    public void removeTaskAll() {
        super.removeTaskAll();
        save();
    }

    @Override
    public void removeSubtaskAll() {
        super.removeSubtaskAll();
        save();
    }

    @Override
    public void removeEpicAll() {
        super.removeEpicAll();
        save();
    }

    @Override
    public Task deleteTask(int id) {
        Task task = super.deleteTask(id);
        save();
        return task;
    }

    @Override
    public Subtask deleteSubtask(int id) {
        Subtask subtask = super.deleteSubtask(id);
        save();
        return subtask;
    }

    @Override
    public Epic deleteEpic(int id) {
        Epic epic = super.deleteEpic(id);
        save();
        return epic;
    }

    @Override
    public Task updatedTask(Task updatedTask) {
        super.updatedTask(updatedTask);
        save();
        return updatedTask;
    }

    @Override
    public Subtask updatedSubtask(Subtask updatedSubtask) {
        super.updatedSubtask(updatedSubtask);
        save();
        return updatedSubtask;
    }

    @Override
    public Epic updatedEpic(Epic updatedEpic) {
        super.updatedEpic(updatedEpic);
        save();
        return updatedEpic;
    }
}
