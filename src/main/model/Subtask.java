package main.model;

import main.util.Status;

import java.time.Duration;
import java.time.LocalDateTime;

public class Subtask extends Task {
    private Integer epicId;


    public Subtask(String title, String discription, Status status) {
        super(title, discription, status);
    }

    public Subtask(String title, String discription, Status status, Duration duration, LocalDateTime startTime) {
        super(title, discription, status, duration, startTime);
    }

    @Override
    public String toString() {
        return super.toString() + "," + epicId;
    }

    public static Subtask fromString(String value) {
        String[] obj = value.split(",");
        Subtask subtask = new Subtask(obj[2], obj[4], Status.valueOf(obj[3]));
        subtask.setId(Integer.parseInt(obj[0]));
        subtask.setEpicId(Integer.parseInt(obj[5]));
        return subtask;
    }

    public void setEpicId(int id) {
        epicId = id;
    }

    public Integer getEpicId() {
        return epicId;
    }
}
