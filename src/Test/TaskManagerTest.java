package Test;

import main.*;
import java.util.ArrayList;
import main.controllers.Managers;
import main.controllers.TaskManager;
import main.model.*;
import main.util.IdGenerate;
import main.util.Status;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class TaskManagerTest {
    private TaskManager taskManager;

    @BeforeEach
    public void beforeEach() {
        taskManager =  Managers.getDefaultTaskManager();
        IdGenerate.resetId();
    }

    @Test
    void tasksWithAnEqualIndexMustBeEqual() {
        Task task1 = new Task("Задача1", "Описание1", Status.NEW);
        Task task2 = new Task("Задача1", "Описание1", Status.NEW);
        Assertions.assertEquals(task1, task2, "ОШИБКА С СРАВНЕНИЕМ ЗАДАЧ КЛАССОВ TASK");
        task2.setId(2);
        boolean equ = task1.equals(task2);
        Assertions.assertFalse(equ);
    }

    @Test
    void heirsTasksWithAnEqualIndexMustBeEqual() {
        Epic epic1 = new Epic("Задача1", "Описание1", Status.NEW);
        Epic epic2 = new Epic("Задача1", "Описание1", Status.NEW);
        Assertions.assertEquals(epic1, epic2, "ОШИБКА С СРАВНЕНИЕМ ЗАДАЧ КЛАССОВ EPIC");
        epic1.setId(2);
        boolean equ1 = epic1.equals(epic2);
        Assertions.assertFalse(equ1);
        Subtask subtask1 = new Subtask("Задача1", "Описание1", Status.NEW);
        Subtask subtask2 = new Subtask("Задача1", "Описание1", Status.NEW);
        Assertions.assertEquals(subtask1, subtask2, "ОШИБКА С СРАВНЕНИЕМ ЗАДАЧ КЛАССОВ EPIC");
        subtask2.setId(3);
        boolean equ2 = subtask1.equals(subtask2);
        Assertions.assertFalse(equ2, "ОШИБКА С СРАВНЕНИЕМ ЗАДАЧ КЛАССОВ SUBTASK");
    }

    @Test
    void subtaskShouldNotMakeYourEpicSybtask() {
        Subtask subtask1 = new Subtask("Задача1", "Описание1", Status.NEW);
        Subtask subtask2 = new Subtask("Задача1", "Описание1", Status.NEW);
        subtask1.setId(2);
        taskManager.addSubtask(subtask2, 2);
        Assertions.assertNull(subtask2.getEpicId(), "Subtask добавлен, как подзадача");
    }

    @Test
    void theManagersClassShouldReturnInitializedManagers() {
        TaskManager taskManager1 = Managers.getDefaultTaskManager();
        Assertions.assertNotNull(taskManager1, "Менеджер не найден");
        Task task1 = new Task("Задача1", "Описание1", Status.NEW);
        taskManager1.addTask(task1);
        Assertions.assertEquals(0, task1.getId(), "У Managers неправильно работают методы");
    }

    @Test
    void inMemoryTaskManagerMustAddDifferentTypesOfTasks() {
        Task task1 = new Task("Задача1", "Описание1", Status.NEW);
        Epic epic1 = new Epic("Задача2", "Описание2", Status.NEW);
        Subtask subtask1 = new Subtask("Задача3", "Описание3", Status.NEW);
        Task task2 = new Task("Задача4", "Описание4", Status.NEW);
        Epic epic2 = new Epic("Задача5", "Описание5", Status.NEW);
        Subtask subtask2 = new Subtask("Задача6", "Описание6", Status.NEW);
        TaskManager taskManager1 = Managers.getDefaultTaskManager();

        taskManager1.addTask(task1);
        taskManager1.addTask(task2);

        taskManager1.addEpic(epic1);
        taskManager1.addEpic(epic2);

        taskManager1.addSubtask(subtask1, 2);
        taskManager1.addSubtask(subtask2, 3);

        Assertions.assertEquals(task2, taskManager1.getTask(task2.getId()), "Менеджер неправильно ищет Task по индексу");
        Assertions.assertEquals(epic2, taskManager1.getEpic(epic2.getId()), "Менеджер неправильно ищет Epic по индексу");
        Assertions.assertEquals(subtask2, taskManager1.getSubtask(subtask2.getId()), "Менеджер неправильно ищет Subtask по индексу");

        ArrayList<Task> tasks = taskManager1.getTasks();
        ArrayList<Epic> epics = taskManager1.getEpics();
        ArrayList<Subtask> subtasks = taskManager1.getSubtasks();

        Assertions.assertNotNull(tasks, "Задачи не возвращаются");
        Assertions.assertNotNull(epics, "Эпики не возвращаются");
        Assertions.assertNotNull(subtasks, "Подзадачи не возвращаются");
    }
    @Test
    void tasksWithTheSpecifiedGeneratedIdShouldNotConflict() {
        Task task1 = new Task("Задача1", "Описание1", Status.NEW);
        Task task2 = new Task("Задача1", "Описание1", Status.NEW);
        task1.setId(2);
        taskManager.addTask(task1);
        taskManager.addTask(task2);
        Assertions.assertEquals(0, task1.getId(), "Неверно генерируется id для задач с заданным id");
        Assertions.assertEquals(1, task2.getId(), "Неверно генерируется id");
    }
    @Test
    void historyManagerMustSaveThePreviousVersionOfTheTask() {
        Task task = new Task("Задача1", "Описание1", Status.NEW);
        Epic epic = new Epic("Задача2", "Описание2", Status.NEW);
        taskManager.addTask(task);
        taskManager.addEpic(epic);
        taskManager.getTask(task.getId());
        taskManager.getEpic(epic.getId());
        System.out.println(taskManager.getHistory().get(0).getId());
        System.out.println(taskManager.getHistory().get(1).getId());
        task.setId(4);
        task.setStatus(Status.IN_PROGRESS);
        epic.setId(7);
        epic.setStatus(Status.IN_PROGRESS);
        Assertions.assertEquals(0, taskManager.getHistory().get(0).getId());
        Assertions.assertEquals(Status.NEW, taskManager.getHistory().get(0).getStatus());
        Assertions.assertEquals(1, taskManager.getHistory().get(1).getId());
        Assertions.assertEquals(Status.NEW, taskManager.getHistory().get(1).getStatus());
    }
    @Test
    void theTasksMustBeUnchangedInAllFields() {
        Task task = new Task("Задача1", "Описание1", Status.NEW);
        taskManager.addTask(task);
        task.setStatus(Status.IN_PROGRESS);
        task.setTitle("aaa");
        task.setDiscription("bbb");
        task.setId(10);
        Assertions.assertEquals("Задача1", taskManager.getTask(0).getTitle());
        Assertions.assertEquals("Описание1", taskManager.getTask(0).getDiscription());
        Assertions.assertEquals(Status.NEW, taskManager.getTask(0).getStatus());
        Assertions.assertEquals(0, taskManager.getTask(0).getId());
    }

}
