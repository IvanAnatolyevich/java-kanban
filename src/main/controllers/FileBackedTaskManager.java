package main.controllers;

import main.model.Epic;
import main.model.Subtask;
import main.model.Task;
import main.util.IdGenerate;

import java.io.*;
import java.nio.file.Files;
import java.util.List;

public class FileBackedTaskManager extends InMemoryTaskManager {
    public FileBackedTaskManager(HistoryManager historyManager) {
        super(historyManager);
    }

    public void save() throws ManagerSaveException {
        try (Writer fileWriter = new FileWriter("File.SCV")) {
            if (!getTasks().isEmpty()) {
                for (Task el: tasks.values()) {
                    fileWriter.write(el.toString() + "\n");
                }
            }
            if (!getEpics().isEmpty()) {
                for (Epic el: epics.values()) {
                    fileWriter.write(el.toString() + "\n");
                }
            }
            if (!getSubtasks().isEmpty()) {
                for (Subtask el: subtasks.values()) {
                    fileWriter.write(el.toString() + "\n");
                }
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
    public Task addTask(Task newTask) throws ManagerSaveException {
        Task task = super.addTask(newTask);
        save();
        return task;
    }

    @Override
    public Subtask addSubtask(Subtask newSuptask, int epicId) throws ManagerSaveException {
        for (Integer i: epics.keySet()) {
            if (epicId == i) {
                newSuptask.setEpicId(epicId);
                break;
            }
        }
        if (newSuptask.getEpicId() == null) {
            System.out.println("Введен неверный индекс эпика.");
            return newSuptask;
        }
        Subtask subtask = new Subtask(newSuptask.getTitle(), newSuptask.getDiscription(), newSuptask.getStatus());
        newSuptask.setId(IdGenerate.generationNewId());
        subtask.setId(newSuptask.getId());
        subtask.setEpicId(newSuptask.getEpicId());
        epics.get(epicId).getSubtasks().add(subtask.getId());
        subtasks.put(subtask.getId(), subtask);
        save();
        return subtask;
    }

    @Override
    public Epic addEpic(Epic newEpic) throws ManagerSaveException {
        Epic epic = super.addEpic(newEpic);
        save();
        return epic;
    }

    @Override
    public void removeTaskAll() throws ManagerSaveException {
        tasks.clear();
        save();
    }

    @Override
    public void removeSubtaskAll() throws ManagerSaveException {
        for (Epic el: epics.values()) {
            for (int id: subtasks.keySet()) {
                if (el.getSubtasks().contains(el.getSubtasks().get(id))) {
                    el.getSubtasks().remove(id);
                    changeEpicStatus(el);
                }
            }
        }
        subtasks.clear();
        save();
    }

    @Override
    public void removeEpicAll() throws ManagerSaveException {
        epics.clear();
        subtasks.clear();
        save();
    }

    @Override
    public Task deleteTask(int id) throws ManagerSaveException {
        Task task = tasks.get(id);
        tasks.remove(id);
        save();
        return task;
    }

    @Override
    public Subtask deleteSubtask(int id) throws ManagerSaveException {
        Subtask subtask = subtasks.get(id);
        epics.get(subtasks.get(id).getEpicId()).getSubtasks().remove(Integer.valueOf(id));
        changeEpicStatus(epics.get(subtasks.get(id).getEpicId()));
        subtasks.get(id).setId(-1);
        subtasks.remove(subtask.getId());
        save();
        return subtask;
    }

    @Override
    public Epic deleteEpic(int id) throws ManagerSaveException {
        Epic epic = epics.get(id);
        for (Subtask el: subtasks.values()) {
            for (Integer i: epics.get(id).getSubtasks()) {
                if (el.getId() == i) {
                    subtasks.remove(id);
                }
            }
        }
        epics.remove(id);
        save();
        return epic;
    }

    @Override
    public Task updatedTask(Task updatedTask) throws ManagerSaveException {
        tasks.put(updatedTask.getId(), updatedTask);
        save();
        return updatedTask;
    }

    @Override
    public Subtask updatedSubtask(Subtask updatedSubtask) throws ManagerSaveException {
        subtasks.put(updatedSubtask.getId(), updatedSubtask);
        changeEpicStatus(epics.get(updatedSubtask.getEpicId()));
        save();
        return updatedSubtask;
    }

    @Override
    public Epic updatedEpic(Epic updatedEpic) throws ManagerSaveException {
        changeEpicStatus(updatedEpic);
        epics.put(updatedEpic.getId(), updatedEpic);
        save();
        return updatedEpic;
    }









}
