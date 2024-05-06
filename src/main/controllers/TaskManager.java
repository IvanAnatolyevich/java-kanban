package main.controllers;

import main.exceptions.ManagerSaveException;
import main.model.Epic;
import main.model.Subtask;
import main.model.Task;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Set;

public interface TaskManager {

    Task addTask(Task newTask) throws ManagerSaveException;

    Subtask addSubtask(Subtask newSuptask, int epicId) throws ManagerSaveException;

    Epic addEpic(Epic newEpic) throws ManagerSaveException;

    void removeTaskAll() throws ManagerSaveException;

    void removeSubtaskAll() throws ManagerSaveException;

    void removeEpicAll() throws ManagerSaveException;

    Task deleteTask(int id) throws ManagerSaveException;

    Subtask deleteSubtask(int id) throws ManagerSaveException;

    Epic deleteEpic(int id) throws ManagerSaveException;

    Task getTask(int id) throws ManagerSaveException;

    Subtask getSubtask(int id) throws ManagerSaveException;

    Epic getEpic(int id) throws ManagerSaveException;

    Task updatedTask(Task updatedTask) throws ManagerSaveException;

    Subtask updatedSubtask(Subtask updatedSubtask) throws ManagerSaveException;

    Epic updatedEpic(Epic updatedEpic) throws ManagerSaveException;

    ArrayList<Task> getTasks();

    ArrayList<Subtask> getSubtasks();

    ArrayList<Epic> getEpics();

    ArrayList<Task> getHistory();

    LocalDateTime getEndTask(Task task);

    LocalDateTime getEndSubtask(Subtask subtask);

    public LocalDateTime getEndTimeEpic(Epic epic);



    Set<Task> getPrioritizedTasks();

    Set<Epic> getPrioritizedEpics();

    Set<Subtask> getPrioritizedSubtasks();

    boolean isIntersectingTask(Task task1, Task task2);

    boolean isIntersectingEpic(Epic epic1, Epic epic2);

    boolean isIntersectingSubtask(Subtask subtask1, Subtask subtask2);
}
