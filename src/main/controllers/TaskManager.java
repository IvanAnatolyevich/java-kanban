package main.controllers;

import main.model.Epic;
import main.model.Subtask;
import main.model.Task;
import java.util.ArrayList;

public interface TaskManager {

    Task addTask(Task newTask);

    Subtask addSubtask(Subtask newSuptask, int epicId);

    Epic addEpic(Epic newEpic);

    void removeTaskAll();

    void removeSubtaskAll();

    void removeEpicAll();

    Task deleteTask(int id);

    Subtask deleteSubtask(int id);

    Epic deleteEpic(int id);

    Task getTask(int id);

    Subtask getSubtask(int id);

    Epic getEpic(int id);

    Task updatedTask(Task updatedTask);

    Subtask updatedSubtask(Subtask updatedSubtask);

    Epic updatedEpic(Epic updatedEpic);

    ArrayList<Task> getTasks();

    ArrayList<Subtask> getSubtasks();

    ArrayList<Epic> getEpics();

    ArrayList<Task> getHistory();
}
