package main.model;

import main.util.Status;

public class Subtask extends Task {
    private Integer epicId;
    public Subtask(String title, String discription, Status status) {
        super(title, discription, status);
    }
    public void setEpicId(int id) {
        epicId = id;
    }
    public Integer getEpicId() {
        return epicId;
    }
}
