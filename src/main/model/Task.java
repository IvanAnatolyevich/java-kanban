package main.model;
import main.util.Status;

import java.util.Objects;

public class Task {
    private int id;
   private Status status;
   private String discription;
   private String title;

   public Task(String title, String discription) {
       this.title = title;
       this.discription = discription;
   }

   public Task(String title, String discription, Status status) {
       this.title = title;
       this.discription = discription;
       this.status = status;
   }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Task task = (Task) obj;
        return Objects.equals(title, task.title) &&
                Objects.equals(discription, task.discription) &&
                Objects.equals(status, task.status) &&
                Objects.equals(id, task.id);
    }


    @Override
    public int hashCode() {
        return Objects.hash(title, discription, status, id);
    }

    @Override
    public String toString() {
        return id + "," + getClass().getSimpleName() + "," + title + "," + status + "," + discription;
    }

    public static Task fromString(String value) {
       String[] obj = value.split(",");
       Task task = new Task(obj[2], obj[4]);
       task.setId(Integer.parseInt(obj[0]));
       task.setStatus(Status.valueOf(obj[3]));
       return task;
    }

    public void setStatus(Status status) {
       this.status = status;
   }

   public Status getStatus() {
       return status;
   }

    public int getId() {
        return id;
    }

    public void setId(int id) {
       this.id = id;
    }

    public String getTitle() {
       return title;
    }

    public String getDiscription() {
       return discription;
    }

    public void setTitle(String title) {
       this.title = title;
    }

    public void setDiscription(String discription) {
       this.discription = discription;
    }
}



