public class Task {
    public int id;
   Status status;
   String discription;
   public Task(String discription) {
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

}



