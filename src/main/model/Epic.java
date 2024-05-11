package main.model;

import main.util.Status;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class Epic extends Task {
private ArrayList<Integer> subtasks = new ArrayList<>();
private LocalDateTime endTime = null;


public Epic(String title, String discription) {
    super(title, discription);
}

public Epic(String title, String discription, Status status) {
        super(title, discription, status);
}

public Epic(String title, String discription, Status status, Duration duration, LocalDateTime startTime) {
    super(title, discription, status, duration, startTime);
}



public static Task fromString(String value) {
    String[] obj = value.split(",");
    Epic epic = new Epic(obj[2], obj[4]);
    epic.setId(Integer.parseInt(obj[0]));
    epic.setStatus(Status.valueOf(obj[3]));
    return epic;
}

public ArrayList<Integer> getSubtasks() {
    return subtasks;
}

public void setEndTime(LocalDateTime endTime) {
    this.endTime = endTime;
}

}

