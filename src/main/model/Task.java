package main.model;
import main.util.Status;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class Task {
    private int id;
   private Status status;
   private String discription;
   private String title;
   private Duration duration;
   private LocalDateTime startTime;

   public Task(String title, String discription) {
       this.title = title;
       this.discription = discription;
   }

   public Task(String title, String discription, Status status) {
       this.title = title;
       this.discription = discription;
       this.status = status;
   }

    public Task(String title, String discription, Status status, Duration duration, LocalDateTime startTime) {
        this.title = title;
        this.discription = discription;
        this.status = status;
        this.duration = duration;
        this.startTime = startTime;
    }

    public Task(int id, Status status, String discription, String title,
                Duration duration, LocalDateTime startTime) {
        this.id = id;
        this.status = status;
        this.discription = discription;
        this.title = title;
        this.duration = duration;
        this.startTime = startTime;
    }


    public Task(Task task) {
        this.status = task.getStatus();
        this.discription = task.getDiscription();
        this.title = task.getTitle();
        this.duration = task.getDuration();
        this.startTime = task.getStartTime();
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
        String str = id + "," + getClass().getSimpleName() + "," + title + "," + status + "," + discription;
                if (startTime != null) {
                    if (duration != null) {
                        return str + "," + startTime.format(DateTimeFormatter.ofPattern("dd.MM.yyyy, HH:mm")) +
                                "," + duration.toMinutes();
                    }
                    return str + "," + startTime.format(DateTimeFormatter.ofPattern("dd.MM.yyyy, HH:mm"));
                } else if (duration != null) {
                    return str + "," + "," + duration.toMinutes();
                }
                return str;
   }

    public static Task fromString(String value) {
       String[] obj = value.split(",");
       Task task = new Task(obj[2], obj[4]);
       task.setId(Integer.parseInt(obj[0]));
       task.setStatus(Status.valueOf(obj[3]));
       if (obj.length > 5 && !obj[5].isBlank()) {
           task.setStartTime(LocalDateTime.parse(obj[5]));
       }
       if (obj.length > 6 && !obj[6].isBlank()) {
           task.setDuration(Duration.ofMinutes(Integer.parseInt(obj[6])));
       }
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

    public LocalDateTime getStartTime() {
       return startTime;
    }

    public void setDuration(Duration duration) {
       this.duration = duration;
    }

    public void setStartTime(LocalDateTime startTime) {
       this.startTime = startTime;
    }

    public Duration getDuration() {
       return duration;
    }

    public LocalDateTime getEndTask() {
        return getStartTime().plus(getDuration());
    }


}



