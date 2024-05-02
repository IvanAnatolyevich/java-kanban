package main.controllers;
import main.util.IdGenerate;
import main.util.Status;
import main.model.*;
import java.util.HashMap;
import java.util.ArrayList;

class InMemoryTaskManager implements TaskManager {
protected static HashMap<Integer, Task> tasks = new HashMap<>();
protected static HashMap<Integer, Subtask> subtasks = new HashMap<>();
protected static HashMap<Integer, Epic> epics = new HashMap<>();
protected HistoryManager historyManager;

public InMemoryTaskManager(HistoryManager historyManager) {
   this.historyManager = historyManager;
}


   @Override
   public Task addTask(Task newTask) throws ManagerSaveException{
      Task task = new Task(newTask.getTitle(), newTask.getDiscription(), newTask.getStatus());
      newTask.setId(IdGenerate.generationNewId());
      task.setId(newTask.getId());
      tasks.put(task.getId(), task);
      return task;
   }

   @Override
   public Subtask addSubtask(Subtask newSuptask, int epicId) throws ManagerSaveException{
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
      changeEpicStatus(epics.get(epicId));
      return subtask;
   }

   @Override
   public Epic addEpic(Epic newEpic) throws ManagerSaveException{
      Epic epic = new Epic(newEpic.getTitle(), newEpic.getDiscription());
      newEpic.setId(IdGenerate.generationNewId());
      epic.setId(newEpic.getId());
      epic.setStatus(newEpic.getStatus());
      epics.put(epic.getId(), epic);
      return epic;
   }

   @Override
   public void removeTaskAll() throws ManagerSaveException{
      tasks.clear();
   }

   @Override
   public void removeSubtaskAll() throws ManagerSaveException{
      for (Epic el: epics.values()) {
         for (int id: subtasks.keySet()) {
            if (el.getSubtasks().contains(el.getSubtasks().get(id))) {
               el.getSubtasks().remove(id);
               changeEpicStatus(el);
            }
         }
      }
      subtasks.clear();
   }

   @Override
   public void removeEpicAll() throws ManagerSaveException{
      epics.clear();
      subtasks.clear();
   }

   @Override
   public Task deleteTask(int id) throws ManagerSaveException{
      Task task = tasks.get(id);
      tasks.remove(id);
      return task;
   }

   @Override
   public Subtask deleteSubtask(int id) throws ManagerSaveException{
      Subtask subtask = subtasks.get(id);
      epics.get(subtasks.get(id).getEpicId()).getSubtasks().remove(Integer.valueOf(id));
      changeEpicStatus(epics.get(subtasks.get(id).getEpicId()));
      subtasks.get(id).setId(-1);
      subtasks.remove(subtask.getId());
      return subtask;
   }

   @Override
   public Epic deleteEpic(int id) throws ManagerSaveException{
      Epic epic = epics.get(id);
      for (Subtask el: subtasks.values()) {
         for (Integer i: epics.get(id).getSubtasks()) {
            if (el.getId() == i) {
               subtasks.remove(id);
            }
         }
      }
      epics.remove(id);
      return epic;
   }

   @Override
   public Task getTask(int id) {
      historyManager.add(tasks.get(id));
       return tasks.get(id);
   }

   @Override
   public Subtask getSubtask(int id) {
      historyManager.add(subtasks.get(id));
       return subtasks.get(id);
   }

   @Override
   public Epic getEpic(int id) {
      historyManager.add(epics.get(id));
      return epics.get(id);
   }

   @Override
   public Task updatedTask(Task updatedTask) throws ManagerSaveException{
      tasks.put(updatedTask.getId(), updatedTask);
      return updatedTask;
   }

   @Override
   public Subtask updatedSubtask(Subtask updatedSubtask) throws ManagerSaveException{
      subtasks.put(updatedSubtask.getId(), updatedSubtask);
      changeEpicStatus(epics.get(updatedSubtask.getEpicId()));
      return updatedSubtask;
   }

   @Override
   public Epic updatedEpic(Epic updatedEpic) throws ManagerSaveException{
      changeEpicStatus(updatedEpic);
      epics.put(updatedEpic.getId(), updatedEpic);
      return updatedEpic;
   }

   @Override
   public ArrayList<Task> getTasks() {
      return new ArrayList<>(tasks.values());
   }

   @Override
   public ArrayList<Subtask> getSubtasks() {
      ArrayList<Subtask> tasks = new ArrayList<>(subtasks.values());
      return tasks;
   }

   @Override
   public ArrayList<Epic> getEpics() {
      ArrayList<Epic> tasks = new ArrayList<>(epics.values());
      return tasks;
   }

   @Override
   public ArrayList<Task> getHistory() {
      return historyManager.getHistory();
   }

   protected void changeEpicStatus(Epic epic) {
      if (epic.getSubtasks().isEmpty()) {
         return;
      }
      int countNew = 0;
      int countDone = 0;
      for (Integer id: epic.getSubtasks()) {
         switch (subtasks.get(id).getStatus()) {
            case Status.NEW:
               countNew++;
               break;
            case Status.IN_PROGRESS:
               epic.setStatus(Status.IN_PROGRESS);
               return;
            case Status.DONE:
               countDone++;
               break;
         }
         if (countNew == epic.getSubtasks().size()) {
            return;
         }
         if (countDone == epic.getSubtasks().size()) {
            epic.setStatus(Status.DONE);
         }
      }
   }
}


