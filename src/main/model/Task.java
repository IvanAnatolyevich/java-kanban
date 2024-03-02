package main.model;
import main.util.Status;

public class Task {
    private int id;
   private Status status;
   private String discription;
   private String title;
   public Task(String title, String discription) {
       this.title = title;
       this.discription = discription;
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
    public void statusIn_Progress() {
       status = Status.IN_PROGRESS;
    }
    public void statusDone() {
       status = Status.DONE;
    }

}



