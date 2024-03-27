package main.model;
import main.util.Status;

import java.util.Objects;

public class Task {
    private int id;
   private Status status;
   private String discription;
   private String title;

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
        return title;
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



