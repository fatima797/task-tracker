package com.github.fatima797.tasktracker.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import com.github.fatima797.tasktracker.model.Status;
import com.github.fatima797.tasktracker.model.Task;
import com.github.fatima797.tasktracker.repository.TaskRepository;

public class TaskManager {
	private final TaskRepository repository;
	private List<Task> tasks = new ArrayList<>();
	private AtomicInteger nextId = new AtomicInteger(0);

	public TaskManager(TaskRepository repository) {
		this.repository = repository;
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

		System.out.println("Task added successfully (ID: " + newTask.getId() + ")");
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
				System.out.println("Task " + task.getId() + " deleted successfully");
				found = true;
				break;
			}
		}

		// Only persist change if a task was found
		if(found) {
			repository.save(tasks);
		}else {
			System.out.println("Task with id " + id + " not found.");
		}	
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

		if(found) {
			repository.save(tasks);
			System.out.println("Task ID " + id + " updated successfully.");
		}else {
			System.out.println("Task ID " + id + " not found.");
		}
	}

	public void listByStatus(String statusInput) {
		// Convert Status and perform error checking 
		Status desiredStatus;
		try {
			// Convert to enum and store the result
			desiredStatus = Status.valueOf(statusInput.toUpperCase());
		} catch (IllegalArgumentException e) {
			System.out.println("Error: Invalid status. Valid options are: " + Arrays.toString(Status.values()));
			return; // Exit if the input is invalid
		}

		if(tasks.isEmpty()) {
			System.out.println("No tasks available.");
			return;
		}

		System.out.println("=== Tasks with status: " + desiredStatus.name() + " ===");
		boolean found = false;

		// Loop and Filter
		for(Task task : tasks) {
			// Check if task's actual status matches the pre-converted desiredStatus
			if(task.getStatus() == desiredStatus) {
				System.out.println(task.toDisplayString());
				found = true;
			}
		}

		// Print Not Found Message (if necessary)
		if(!found) {
			System.out.println("No task found with status: " + desiredStatus.name());
		}
	}

	public void listAll() {
		if (tasks.isEmpty()) {
	        System.out.println("No tasks available.");
	        return;
	    }
		
		System.out.println("=== All Tasks ===");
		for (Task task : tasks) {
			System.out.println(task.toDisplayString());

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
		
		if(found) {
			repository.save(tasks);
			System.out.println("Task " + id + " marked as " + newStatus);
		}else {
			System.out.println("Task " + id + " not found.");
		}
		
	}
	
	public void markTaskAsDone(int id) {
		updateTaskStatus(id, Status.DONE);
		
	}
	
	public void markInProgress(int id) {
		updateTaskStatus(id, Status.IN_PROGRESS);
	}
}
