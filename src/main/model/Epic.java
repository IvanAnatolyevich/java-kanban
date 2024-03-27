package main.model;

import java.util.ArrayList;

public class Epic extends Task {
private ArrayList<Integer> subtasks = new ArrayList<>();

public Epic(String title, String discription) {
    super(title, discription);
}

public ArrayList<Integer> getSubtasks() {
    return subtasks;
}

}

