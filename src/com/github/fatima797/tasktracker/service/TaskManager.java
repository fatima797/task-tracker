package com.github.fatima797.tasktracker.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import com.github.fatima797.tasktracker.model.Status;
import com.github.fatima797.tasktracker.model.Task;
import com.github.fatima797.tasktracker.repository.TaskRepository;
import com.github.fatima797.tasktracker.view.ConsoleView;

public class TaskManager {
	private final TaskRepository repository;
	private final ConsoleView view;
	private List<Task> tasks = new ArrayList<>();
	private AtomicInteger nextId = new AtomicInteger(0);

	public TaskManager(TaskRepository repository, ConsoleView view) {
		this.repository = repository;
		this.view = view;
		this.tasks = repository.load();
		
		// Determine highest ID from loaded tasks
		int maxId = tasks.stream()
				.mapToInt(Task::getId)
				.max()
				.orElse(0); // If no tasks available, start at 0
		
		this.nextId.set(maxId);
	}

	public void addTask(String description) {

		// Generate unique ID based on highest existing task ID
		int newId = generateId();
		Task newTask = new Task(newId, description);
		// Add new task to in-memory list
		tasks.add(newTask);

		// Save task list to JSON file
		repository.save(tasks);

		view.showTaskAdded(newTask);
	}

	private int generateId() {
		int id = nextId.incrementAndGet();
		return id;
	}


	public void deleteTask(int id) {
		// Flag to track if task was found and removed
		boolean found = false;

		for(int i = 0; i < tasks.size(); i++) {

			Task task = tasks.get(i);
			if(task.getId() == id) {
				tasks.remove(i);	
				found = true;
				break;
			}
		}
		
		if(!found) {
			view.showError("Task with id" + id + "not found.");
			return;
		}
		
		repository.save(tasks);
		
		view.showTaskDeleted(id);
	
	}

	public void updateTask(int id, String newDescription) {
		boolean found = false;

		for(Task task : tasks) {
			if(task.getId() == id) {
				task.setDescription(newDescription);
				task.updateTimestamp();
				found = true;
				break;
			}
		}
		
		if(!found) {
			view.showError("Task ID " + id + " not found.");
			return;
		}
		
		repository.save(tasks);
		
		view.showTaskUpdated(id);
	}

	public void listByStatus(String statusInput) {
		// Convert Status and perform error checking 
		Status desiredStatus;
		try {
			// Convert to enum and store the result
			desiredStatus = Status.valueOf(statusInput.toUpperCase());
		} catch (IllegalArgumentException e) {
			view.showError("Error: Invalid status. Valid options are: " + Arrays.toString(Status.values()));
			return; // Exit if the input is invalid
		}

		if(tasks.isEmpty()) {
			view.showTaskListIsEmpty();
			return;
		}

		view.showStatusHeader(desiredStatus);
		
		boolean found = false;

		// Loop and Filter
		for(Task task : tasks) {
			// Check if task's actual status matches the pre-converted desiredStatus
			if(task.getStatus() == desiredStatus) {
				view.showTask(task);
				found = true;
			}
		}

		if(!found) {
			view.showError("No task found with status: " + desiredStatus.name());
			return;
		}
	}

	public void listAll() {
		if (tasks.isEmpty()) {
			view.showTaskListIsEmpty();
	        return;
	    }
		
		view.showTaskListHeader();
		for (Task task : tasks) {
			view.showTask(task);
		}
	}
	
	public void updateTaskStatus(int id, Status newStatus) {
		boolean found = false;
		
		for(Task task : tasks) {
			if(task.getId() == id) {
				task.setStatus(newStatus);
				task.updateTimestamp();
				found = true;
				break;
			}
		}
		
		if(!found) {
			view.showError("Task " + id + " not found.");
			return;
		}
		
		repository.save(tasks);
		
		view.showTaskStatusUpdated(id, newStatus);
		
	}

	public void markTaskAsDone(int id) {
		updateTaskStatus(id, Status.DONE);
		
	}
	
	public void markInProgress(int id) {
		updateTaskStatus(id, Status.IN_PROGRESS);
	}
}
