package main.controllers;

import main.model.Task;

import java.util.ArrayList;

public class InMemoryHistoryManager implements HistoryManager{
    private ArrayList<Task> history = new ArrayList<>();
    @Override
    public void add(Task task) {
        history.add(task);
    }
    @Override
    public ArrayList<Task> getHistory() {
        return history;
    }
}
