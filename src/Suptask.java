public class Suptask extends Task {
    private Integer epicId;
    public Suptask(String discription) {
        super(discription);
    }
    public void setEpicId(int id) {
        epicId = id;
    }
    public int getEpicId() {
        return epicId;
    }
}
