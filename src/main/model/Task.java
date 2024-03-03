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



