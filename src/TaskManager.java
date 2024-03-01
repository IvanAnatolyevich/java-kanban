import java.util.HashMap;
public class TaskManager {
HashMap<Integer, Task> tasks = new HashMap<>();
HashMap<Integer, Suptask> suptasks = new HashMap<>();
HashMap<Integer, Epic> epics = new HashMap<>();
   public Task addTask(Task newTask) {
      newTask.setStatus(Status.NEW);
      newTask.setId(IdGenerate.generationNewId());
      tasks.put(newTask.getId(), newTask);
      return newTask;
   }
   public Suptask addSuptask(Suptask newSuptask, int epicId) {
      newSuptask.setId(IdGenerate.generationNewId());
      newSuptask.setEpicId(epicId);
      newSuptask.setStatus(Status.NEW);
      epics.get(epicId).suptasks.add(newSuptask.getId());
      suptasks.put(newSuptask.getId(), newSuptask);
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
   public void removeSuptaskAll() {
      suptasks.clear();
   }
   public void removeEpicAll() {
      epics.clear();
   }
   public Task deleteTask(int id) {
      Task task = tasks.get(id);
      tasks.get(id).setStatus(Status.DONE);
      tasks.remove(id);
      return task;
   }
   public Suptask deleteSuptask(int id) {
      Suptask suptask = suptasks.get(id);
      suptasks.get(id).setStatus(Status.DONE);
      suptasks.remove(id);
      return suptask;
   }
   public Epic deleteEpic(int id) {
      Epic epic = epics.get(id);
      epics.get(id).setStatus(Status.DONE);
      epics.remove(id);
      return epic;
   }
   public Task getTask(int id) {
      return tasks.get(id);
   }
   public Suptask getSuptask(int id) {
      return suptasks.get(id);
   }
   public Epic getEpic(int id) {
      return epics.get(id);
   }
   public void printAllTask() {
      for (int i: tasks.keySet()) {
         System.out.println(tasks.get(i));
      }
   }
   public void printAllSuptask() {
      for (int i: suptasks.keySet()) {
         System.out.println(suptasks.get(i));
      }
   }
   public void printAllEpic() {
      for (int i: epics.keySet()) {
         System.out.println(epics.get(i));
      }
   }
   public Task updatedTask(Task updatedTask) {
      //Task currentTask = tasks.get(updatedTask.id);
      updatedTask.setStatus(Status.IN_PROGRESS);
      tasks.put(updatedTask.getId(), updatedTask);
      return updatedTask;
   }
   public Suptask updatedSuptask(Suptask updatedSuptask) {
      //Suptask currentSuptask = suptasks.get(updatedSuptask.id);
      updatedSuptask.setStatus(Status.IN_PROGRESS);
      suptasks.put(updatedSuptask.getId(), updatedSuptask);
      changeEpicStatus(epics.get(updatedSuptask.getEpicId()));
      return updatedSuptask;
   }
   public Epic updatedEpic(Epic updatedEpic) {
      //Epic currentEpic = epics.get(updatedEpic.id);
      changeEpicStatus(updatedEpic);
      epics.put(updatedEpic.getId(), updatedEpic);

      return updatedEpic;
   }
   private void changeEpicStatus(Epic epic) {
      if(epic.suptasks.isEmpty()) {
         return;
      }
      int countNew = 0;
      int countDone = 0;
      for(Integer id: epic.suptasks) {
         switch(suptasks.get(id).getStatus()) {
            case NEW:
               countNew++;
               break;
            case IN_PROGRESS:
               epic.setStatus(Status.IN_PROGRESS);
               return;
            case DONE:
               countDone++;
               break;
         }
         if(countNew == epic.suptasks.size()) {
            return;
         }
         if (countDone == epic.suptasks.size()) {
            epic.setStatus(Status.DONE);
         }
      }
      }
}


