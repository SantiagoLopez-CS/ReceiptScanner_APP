package papertrail.service;

import papertrail.model.Task;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TaskManager {
    // List of Tasks
    private final List<Task> tasks = new ArrayList<>();

    // Add new task
    public void addTask(Task task) {
        tasks.add(task);
    }

    // Remove a task by ID
    public boolean removeTask(String id) {
        return tasks.removeIf(t -> t.getId().equals(id)); // For every t, if ID matches what we were given, remove it
    }

    // Mark a task completed
    public boolean completeTask(String id) {
        Optional<Task> task = tasks.stream() // converts the list into a stream (a flow of data we can filter/search)
                                   .filter(t -> t.getId().equals(id))
                                   .findFirst();
        if (task.isPresent()) {
            task.get().setCompleted(true);
            return true;
        }
        return false;
    }

    // Return a COPY of the current task list
    public List<Task> getAllTasks() {
        return new ArrayList<>(tasks);
    }

    // Return a COPY of just incomplete tasks
    public List<Task> getPendingTasks() {
        return tasks.stream()
                .filter(t -> !t.isCompleted())
                .toList(); // Returns a new list with filtered results
    }

    // Find a task by ID
    public Task getTaskById(String id) {
        return tasks.stream() // Converts list to stream(a flow of data able to filter/search)
                .filter(t -> t.getId().equals(id))
                .findFirst()
                .orElse(null);
    }
}

