package main.controllers;
import main.util.IdGenerate;
import main.util.Status;
import main.model.*;


import java.util.HashMap;
import java.util.ArrayList;
public class TaskManager {
private HashMap<Integer, Task> tasks = new HashMap<>();
private HashMap<Integer, Subtask> subtasks = new HashMap<>();
private HashMap<Integer, Epic> epics = new HashMap<>();
   public Task addTask(Task newTask) {
      newTask.setStatus(Status.NEW);
      newTask.setId(IdGenerate.generationNewId());
      tasks.put(newTask.getId(), newTask);
      return newTask;
   }
   public Subtask addSubtask(Subtask newSuptask, int epicId) {
      newSuptask.setId(IdGenerate.generationNewId());
      newSuptask.setEpicId(epicId);
      newSuptask.setStatus(Status.NEW);
      epics.get(epicId).getSubtasks().add(newSuptask.getId());
      subtasks.put(newSuptask.getId(), newSuptask);
      changeEpicStatus(epics.get(epicId));
      return newSuptask;
   }
   public Epic addEpic(Epic newEpic) {
      newEpic.setId(IdGenerate.generationNewId());
      newEpic.setStatus(Status.NEW);
      epics.put(newEpic.getId(), newEpic);
      return newEpic;
   }
   public void removeTaskAll() {
      tasks.clear();
   }
   public void removeSubtaskAll() {
      for(Epic el: epics.values()) {
         for (int id: subtasks.keySet()) {
            if(el.getSubtasks().contains(el.getSubtasks().get(id))) {
               el.getSubtasks().remove(id);
               changeEpicStatus(el);
            }
         }
      }
      subtasks.clear();
   }
   public void removeEpicAll() {
      epics.clear();
      subtasks.clear();
   }
   public Task deleteTask(int id) {
      Task task = tasks.get(id);
      tasks.get(id).setStatus(Status.DONE);
      tasks.remove(id);
      return task;
   }
   public Subtask deleteSubtask(int id) {
      Subtask subtask = subtasks.get(id);
      //suptasks.get(id).setStatus(main.controllers.util.Status.DONE);
      epics.get(subtasks.get(id).getEpicId()).getSubtasks().remove(id);
      changeEpicStatus(epics.get(subtasks.get(id).getEpicId()));
      subtasks.remove(id);
      return subtask;
   }
   public Epic deleteEpic(int id) {
      Epic epic = epics.get(id);
      //epics.get(id).setStatus(main.controllers.util.Status.DONE);
      for(Subtask el: subtasks.values()) {
         for(Integer i: epics.get(id).getSubtasks()) {
            if (el.getId() == i) {
               subtasks.remove(id);
            }
         }
      }
      epics.remove(id);
      return epic;
   }
   public Task getTask(int id) {
      return tasks.get(id);
   }
   public Subtask getSubtask(int id) {
      return subtasks.get(id);
   }
   public Epic getEpic(int id) {
      return epics.get(id);
   }
   public Task updatedTask(Task updatedTask) {
      //main.controllers.model.Task currentTask = tasks.get(updatedTask.id);
      //updatedTask.setStatus(main.controllers.util.Status.IN_PROGRESS);
      tasks.put(updatedTask.getId(), updatedTask);
      return updatedTask;
   }
   public Subtask updatedSubtask(Subtask updatedSubtask) {
      //main.controllers.model.Subtask currentSubtask = subtasks.get(updatedSubtask.id);
      //updatedSubtask.setStatus(main.controllers.util.Status.IN_PROGRESS);
      subtasks.put(updatedSubtask.getId(), updatedSubtask);
      changeEpicStatus(epics.get(updatedSubtask.getEpicId()));
      return updatedSubtask;
   }
   public Epic updatedEpic(Epic updatedEpic) {
      //main.controllers.model.Epic currentEpic = epics.get(updatedEpic.id);
      changeEpicStatus(updatedEpic);
      epics.put(updatedEpic.getId(), updatedEpic);

      return updatedEpic;
   }
   public ArrayList<Task> getTasks() {
      return new ArrayList<>(tasks.values());
   }
   public ArrayList<Subtask> getSubtasks() {
      ArrayList<Subtask> tasks = new ArrayList<>(subtasks.values());
      return tasks;
   }
   public ArrayList<Epic> getEpics() {
      ArrayList<Epic> tasks = new ArrayList<>(epics.values());
      return tasks;
   }
   private void changeEpicStatus(Epic epic) {
      if(epic.getSubtasks().isEmpty()) {
         return;
      }
      int countNew = 0;
      int countDone = 0;
      for(Integer id: epic.getSubtasks()) {
         switch(subtasks.get(id).getStatus()) {
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
         if(countNew == epic.getSubtasks().size()) {
            return;
         }
         if (countDone == epic.getSubtasks().size()) {
            epic.setStatus(Status.DONE);
         }
      }
      }
}


