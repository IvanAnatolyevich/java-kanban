import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;

import main.controllers.FileBackedTaskManager;
import main.exceptions.ManagerSaveException;
import main.controllers.Managers;
import main.controllers.TaskManager;
import main.model.*;
import main.util.IdGenerate;
import main.util.Status;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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
        Epic epic1 = new Epic("Задача1", "Описание1");
        Epic epic2 = new Epic("Задача1", "Описание1");
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

//    @Test
//    void subtaskShouldNotMakeYourEpicSybtask() throws ManagerSaveException {
//        Subtask subtask1 = new Subtask("Задача1", "Описание1", Status.NEW);
//        Subtask subtask2 = new Subtask("Задача1", "Описание1", Status.NEW);
//        subtask1.setId(2);
//        taskManager.addSubtask(subtask2, 2);
//        Assertions.assertNull(subtask2.getEpicId(), "Subtask добавлен, как подзадача");
//    }

    @Test
    void theManagersClassShouldReturnInitializedManagers() throws ManagerSaveException {
        TaskManager taskManager1 = Managers.getDefaultTaskManager();
        Assertions.assertNotNull(taskManager1, "Менеджер не найден");
        Task task1 = new Task("Задача1", "Описание1", Status.NEW);
        taskManager1.addTask(task1);
        Assertions.assertEquals(0, task1.getId(), "У Managers неправильно работают методы");
    }

    @Test
    void inMemoryTaskManagerMustAddDifferentTypesOfTasks() {
        Task task1 = new Task("Задача1", "Описание1", Status.NEW);
        Epic epic1 = new Epic("Задача2", "Описание2");
        Subtask subtask1 = new Subtask("Задача3", "Описание3", Status.NEW);
        Task task2 = new Task("Задача4", "Описание4", Status.NEW);
        Epic epic2 = new Epic("Задача5", "Описание5");
        Subtask subtask2 = new Subtask("Задача6", "Описание6", Status.NEW);
        TaskManager taskManager1 = Managers.getDefaultTaskManager();

        taskManager1.addTask(task1);
        taskManager1.addTask(task2);

        taskManager1.addEpic(epic1);
        taskManager1.addEpic(epic2);

        taskManager1.addSubtask(subtask1, 2);
        taskManager1.addSubtask(subtask2, 3);
        epic2.setStatus(Status.NEW);

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
    void tasksWithTheSpecifiedGeneratedIdShouldNotConflict() throws ManagerSaveException {
        Task task1 = new Task("Задача1", "Описание1", Status.NEW);
        Task task2 = new Task("Задача1", "Описание1", Status.NEW);
        task1.setId(2);
        taskManager.addTask(task1);
        taskManager.addTask(task2);
        Assertions.assertEquals(0, task1.getId(), "Неверно генерируется id для задач с заданным id");
        Assertions.assertEquals(1, task2.getId(), "Неверно генерируется id");
    }

    @Test
    void historyManagerMustSaveThePreviousVersionOfTheTask() throws ManagerSaveException {
        Task task = new Task("Задача1", "Описание1", Status.NEW);
        taskManager.addTask(task);
        taskManager.getTask(task.getId());
        task.setId(4);
        task.setStatus(Status.IN_PROGRESS);
        Assertions.assertEquals(0, taskManager.getHistory().get(0).getId());
        Assertions.assertEquals(Status.NEW, taskManager.getHistory().get(0).getStatus());
    }

    @Test
    void theTasksMustBeUnchangedInAllFields() throws ManagerSaveException {
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

    @Test
    void epicsSouldNotStoreIrrelevantSubtasks() throws ManagerSaveException {
        Epic epic = new Epic("Задача1", "Описание2");
        Subtask subtask = new Subtask("Задача2", "Описание2", Status.NEW);
        taskManager.addEpic(epic);
        taskManager.addSubtask(subtask, 0);
        taskManager.deleteSubtask(1);
        Assertions.assertEquals(0, epic.getSubtasks().size());
    }

    @Test
    void theBuilt_inLinkedListAndOperationsShouldWorkCorrectly() throws ManagerSaveException {
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
        taskManager.getTask(2);
        Assertions.assertEquals(5, taskManager.getHistory().size());
    }

    @Test
    void settersDoNotAffectTheDataInTheManager() throws ManagerSaveException {
        Task task = new Task("Задача1", "Описание1", Status.NEW);
        task.setId(10);
        taskManager.addTask(task);
        Assertions.assertEquals(0, task.getId());
    }

    @Test
    void fileBackedTaskManagerMustSaveAndLoadAnEmptyFile() throws IOException, ManagerSaveException {
        File.createTempFile("File","SCV");
        FileBackedTaskManager obj = new FileBackedTaskManager(Managers.getDefaultHistoryManager());
        Task task1 = new Task("Задача1", "Описание1", Status.NEW);
        Epic epic = new Epic("Задача2", "Описание2", Status.NEW);
        Subtask subtask = new Subtask("Задача3", "Описание3", Status.NEW);

        obj.addTask(task1);
        obj.addEpic(epic);
        obj.addSubtask(subtask, 1);
        taskManager.removeTaskAll();
        taskManager.removeEpicAll();
        taskManager.removeSubtaskAll();

        FileBackedTaskManager o = obj.loadFromFile(Paths.get("File.SCV").toFile());
        Assertions.assertEquals(task1, o.getTasks().get(0));
        Assertions.assertEquals(subtask, o.getSubtasks().get(0));
        Assertions.assertEquals(epic, o.getEpics().get(0));
    }

    @Test
    void theStatusMustBeIndicatedCorrectly() {
        Epic epic1 = new Epic("Задача2", "Описание2");
        Epic epic2 = new Epic("Задача2", "Описание2");
        Epic epic3 = new Epic("Задача2", "Описание2");
        Epic epic4 = new Epic("Задача2", "Описание2");


        Subtask subtask1 = new Subtask("Задача3", "Описание3", Status.NEW);
        Subtask subtask2 = new Subtask("Задача3", "Описание3", Status.IN_PROGRESS);
        Subtask subtask3 = new Subtask("Задача3", "Описание3", Status.DONE);
        Subtask subtask4 = new Subtask("Задача3", "Описание3", Status.NEW);
        Subtask subtask5 = new Subtask("Задача3", "Описание3", Status.DONE);


        taskManager.addEpic(epic1);
        taskManager.addEpic(epic2);
        taskManager.addEpic(epic3);
        taskManager.addEpic(epic4);

        taskManager.addSubtask(subtask1, 0);
        taskManager.addSubtask(subtask2, 1);
        taskManager.addSubtask(subtask3,2);
        taskManager.addSubtask(subtask4,3);
        taskManager.addSubtask(subtask5,3);

        Assertions.assertEquals(Status.NEW, taskManager.getEpic(0).getStatus());
        Assertions.assertEquals(Status.IN_PROGRESS, taskManager.getEpic(1).getStatus());
        Assertions.assertEquals(Status.DONE, taskManager.getEpic(2).getStatus());
        Assertions.assertEquals(Status.IN_PROGRESS, taskManager.getEpic(3).getStatus());
    }

    @Test
    void allHistoryManagerMethodsMustBeExecuted() {
        Assertions.assertEquals(new ArrayList<Task>(), taskManager.getHistory());

        Task task1 = new Task("Задача1", "Описание1", Status.NEW);

        taskManager.addTask(task1);
        taskManager.getTask(0);
        taskManager.getTask(0);
        ArrayList<Task> array = taskManager.getHistory();

        Assertions.assertEquals(taskManager.getTasks(), array);

    }

    @Test
    void mustCorrectlyInterceptExceptions() {
        FileBackedTaskManager obj = new FileBackedTaskManager(Managers.getDefaultHistoryManager());
        Task task1 = new Task("Задача1", "Описание1", Status.NEW);
        Assertions.assertThrows(IOException.class, () -> {
            Writer fileWriter = new FileWriter("");
            fileWriter.write(task1.toString());
        }, "Ошибка сохранения файла");
    }

    @Test
    void mustCorrectlyCalculateTheTimeIntersection() {

        Task task = new Task("аоа","dfdgj");
        Task task1 = new Task("аоа","dfdfd");
        Task task2 = new Task("аоа","dfdfd");
        Task task3 = new Task("аоа","dfdfd");

        task.setStartTime(LocalDateTime.of(2020,10,13, 9, 0));
        task.setDuration(Duration.ofHours(2)); //11

        task1.setStartTime(LocalDateTime.of(2020,10,13, 0, 0));
        task1.setDuration(Duration.ofHours(2)); //5

        task2.setStartTime(LocalDateTime.of(2020,10,13, 0, 0));
        task2.setDuration(Duration.ofHours(2)); //2

        task3.setStartTime(LocalDateTime.of(2020,10,13, 6,0));
        task3.setDuration(Duration.ofHours(2)); //8

        taskManager.addTask(task);
        taskManager.addTask(task1);
        taskManager.addTask(task2);
        taskManager.addTask(task3);

        boolean equ = taskManager.getTasks().stream()
                .allMatch((obj1) -> taskManager.isIntersecting(obj1, task));

        Assertions.assertEquals(true, equ);

         equ = taskManager.getTasks().stream()
                .allMatch((obj1) -> taskManager.isIntersecting(obj1, task2));

        Assertions.assertEquals(false, equ);


    }
}
