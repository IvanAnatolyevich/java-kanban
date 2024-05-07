package main.controllers;
import main.util.IdGenerate;
import main.util.Status;
import main.model.*;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

class InMemoryTaskManager implements TaskManager {
   protected HashMap<Integer, Task> tasks = new HashMap<>();
   protected HashMap<Integer, Subtask> subtasks = new HashMap<>();
   protected HashMap<Integer, Epic> epics = new HashMap<>();
   protected HistoryManager historyManager;
   protected Set prioritizedTasks = new TreeSet<>(Comparator.comparing(Task::getStartTime));

   public InMemoryTaskManager(HistoryManager historyManager) {
      this.historyManager = historyManager;
   }


   @Override
   public Task addTask(Task newTask) {
      Task task = new Task(newTask.getTitle(), newTask.getDiscription(), newTask.getStatus());
      newTask.setId(IdGenerate.generationNewId());
      task.setId(newTask.getId());
      task.setStartTime(newTask.getStartTime());
      task.setDuration(newTask.getDuration());
      boolean equ = getPrioritized().stream()
              .allMatch((obj1) -> isIntersecting(obj1, task));
      if (!equ) {
         System.out.println("Введена некорретная дата");
         return task;
      }
      tasks.put(task.getId(), task);
      return task;
   }

   @Override
   public Subtask addSubtask(Subtask newSuptask, int epicId) {
      for (Integer i : epics.keySet()) {
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
      boolean equ = getPrioritized().stream()
              .allMatch((obj1) -> isIntersecting(obj1, subtask));
      if (!equ) {
         System.out.println("Введена некорретная дата");
         return subtask;
      }
      epics.get(epicId).getSubtasks().add(subtask.getId());
      subtasks.put(subtask.getId(), subtask);
      changeEpicStatus(epics.get(epicId));
      return subtask;
   }

   @Override
   public Epic addEpic(Epic newEpic) {
      Epic epic = new Epic(newEpic.getTitle(), newEpic.getDiscription());
      newEpic.setId(IdGenerate.generationNewId());
      epic.setId(newEpic.getId());
      epic.setStatus(newEpic.getStatus());
      boolean equ = getPrioritized().stream()
              .allMatch((obj1) -> isIntersecting(obj1, epic));//
      if (!equ) {
         System.out.println("Введена некорретная дата");
         return epic;
      }
      epics.put(epic.getId(), epic);
      return epic;
   }

   @Override
   public void removeTaskAll() {
      tasks.clear();
   }

   @Override
   public void removeSubtaskAll() {
      epics.values().stream()
              .forEach(epic -> {
                 subtasks.keySet().stream()
                         .filter(obj -> epic.getSubtasks().contains(epic.getSubtasks().get(obj)))
                         .forEach(obj -> epic.getSubtasks().remove(obj));
              });
      subtasks.clear();
   }

   @Override
   public void removeEpicAll() {
      epics.clear();
      subtasks.clear();
   }

   @Override
   public Task deleteTask(int id) {
      Task task = tasks.remove(id);
      prioritizedTasks.remove(task.getId());
      historyManager.remove(id);
      tasks.remove(id);
      return task;
   }

   @Override
   public Subtask deleteSubtask(int id) {
      Subtask subtask = subtasks.get(id);
      epics.get(subtasks.get(id).getEpicId()).getSubtasks().remove(Integer.valueOf(id));
      changeEpicStatus(epics.get(subtasks.get(id).getEpicId()));
      subtasks.get(id).setId(-1);
      prioritizedTasks.remove(id);
      historyManager.remove(id);
      subtasks.remove(subtask.getId());
      return subtask;
   }

   @Override
   public Epic deleteEpic(int id) {
      final Epic epic = epics.remove(id);
         historyManager.remove(id);
         for (Integer subtaskId : epic.getSubtasks()) {
            final Subtask subtask = subtasks.remove(subtaskId);
            historyManager.remove(subtaskId);
            prioritizedTasks.remove(subtask);
         }
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
   public Task updatedTask(Task updatedTask) {
      tasks.put(updatedTask.getId(), updatedTask);
      return updatedTask;
   }

   @Override
   public Subtask updatedSubtask(Subtask updatedSubtask) {
      subtasks.put(updatedSubtask.getId(), updatedSubtask);
      changeEpicStatus(epics.get(updatedSubtask.getEpicId()));
      return updatedSubtask;
   }

   @Override
   public Epic updatedEpic(Epic updatedEpic) {
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
      for (Integer id : epic.getSubtasks()) {
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
            epic.setStatus(Status.NEW);
            return;
         }
         if (countDone == epic.getSubtasks().size()) {
            epic.setStatus(Status.DONE);
         } else if (countDone >= 1) {
            epic.setStatus(Status.IN_PROGRESS);
         }
      }
   }

   @Override
   public LocalDateTime getEndTask(Task task) {
      return task.getStartTime().plus(task.getDuration());
   }

   @Override
   public LocalDateTime getEndSubtask(Subtask subtask) {
      return subtask.getStartTime().plus(subtask.getDuration());
   }

   @Override
   public LocalDateTime getEndTimeEpic(Epic epic) {
      Optional<Subtask> minTimeSubstak = epic.getSubtasks().stream()
              .map(substakId -> {
                 Subtask subtask = subtasks.get(substakId);
                 Optional<Subtask> obj = Optional.ofNullable(subtask);
                 return obj;
              })
              .filter(Optional::isPresent)
              .map(Optional::get)
              .min(Comparator.comparing(substak -> substak.getStartTime()));
      LocalDateTime startTime = minTimeSubstak.get().getStartTime();
      epic.setStartTime(startTime);
      Optional<Subtask> maxTimeSubtask = epic.getSubtasks().stream()
              .map(substakId -> {
                 Subtask subtask = subtasks.get(substakId);
                 Optional<Subtask> obj = Optional.ofNullable(subtask);
                 return obj;
              })
              .filter(Optional::isPresent)
              .map(Optional::get)
              .min(Comparator.comparing(substak -> getEndSubtask(substak)));
      LocalDateTime endTimeEpic  = maxTimeSubtask.get().getEndTask();
      epic.setDuration(Duration.between(startTime, endTimeEpic));
      return endTimeEpic;
   }


   public List<Task> getPrioritized() {
      Set<Task> priority = new TreeSet<>(Comparator.comparing(Task::getStartTime));
         tasks.values().stream().filter(obj ->
                 obj.getStartTime() != null).filter(task -> tasks.values().stream().allMatch(task1 ->
                 isIntersecting(task1, task))).forEach(priority::add);
         subtasks.values().stream().filter(obj ->
                 obj.getStartTime() != null).filter(subtask -> subtasks.values().stream().allMatch(subtask1 ->
                 isIntersecting(subtask1, subtask))).forEach(priority::add);
         prioritizedTasks = priority;
      return new ArrayList<>(priority);
   }

   public boolean isIntersecting(Task task1, Task task2) {
      if (task2.getStartTime().isAfter(task1.getEndTask()) ||
              task2.getEndTask().isBefore(task1.getStartTime()) ||
              task1.equals(task2)) {
         return true;
      }
      return false;
   }
}



