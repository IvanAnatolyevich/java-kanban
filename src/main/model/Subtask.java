package main.model;

import main.util.Status;

public class Subtask extends Task {
    private Integer epicId;


    public Subtask(String title, String discription, Status status) {
        super(title, discription, status);
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
