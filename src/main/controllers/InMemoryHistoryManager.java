package main.controllers;

import main.model.Task;
import java.util.HashMap;
import java.util.ArrayList;

public class InMemoryHistoryManager implements HistoryManager {
    private HashMap<Integer, Node> history = new HashMap<>();
    private Node head = null;
    private Node tail = null;

    private Node linkLast(Task el) {
        Node newNode = new Node(el);
        if (tail == null) {
            head = newNode;
        } else {
            tail.next = newNode;
        }
        newNode.prev = tail;
        tail = newNode;
        return newNode;
    }

    private void removeNode(Node newNode) {
        newNode.prev.next = newNode.next;
        newNode.next.prev = newNode.prev;
        newNode.next = null;
        newNode.prev = null;
    }

    @Override
    public void add(Task task) {
        if (history.containsKey(task.getId())) {
            if (history.get(task.getId()) == head) {
                return;
            } else {
                removeNode(history.get(task.getId()));
                remove(task.getId());
            }
        }
            history.put(task.getId(), linkLast(task));
    }

    private void remove(int id) {
        history.remove(id);
    }

    @Override
    public ArrayList<Task> getHistory() {
        ArrayList<Task> tasks = new ArrayList<>();
        Node el = head;
        while (el != null) {
            tasks.add(el.data);
            el = el.next;
        }
        return tasks;
    }
}
