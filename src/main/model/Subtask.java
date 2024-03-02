package main.model;

public class Subtask extends Task {
    private Integer epicId;
    public Subtask(String title, String discription) {
        super(title, discription);
    }
    public void setEpicId(int id) {
        epicId = id;
    }
    public int getEpicId() {
        return epicId;
    }
}
