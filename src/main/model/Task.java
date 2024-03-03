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
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return Objects.equals(id, task.id) &&
                Objects.equals(status, task.status) &&
                Objects.equals(discription, task.discription) &&
                Objects.equals(title, task.title);
    }

    @Override
    public int hashCode() {
        // вызываем вспомогательный метод и передаём в него нужные поля
        return Objects.hash(id, status, discription, title);
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


}



