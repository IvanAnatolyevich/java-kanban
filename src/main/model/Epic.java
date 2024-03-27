package main.model;

import main.util.Status;

import java.util.ArrayList;

public class Epic extends Task {
private ArrayList<Integer> subtasks = new ArrayList<>();

public Epic(String title, String discription, Status status) {
    super(title, discription, status);
}

public ArrayList<Integer> getSubtasks() {
    return subtasks;
}

}

