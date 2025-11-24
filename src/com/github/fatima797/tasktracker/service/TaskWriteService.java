package com.github.fatima797.tasktracker.service;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import com.github.fatima797.tasktracker.model.Status;
import com.github.fatima797.tasktracker.model.Task;
import com.github.fatima797.tasktracker.repository.TaskRepository;
import com.github.fatima797.tasktracker.view.ConsoleView;

public class TaskWriteService {
	private List<Task> tasks;
	private final TaskRepository repository;
	private final ConsoleView view;
	private AtomicInteger nextId = new AtomicInteger(0);
	
	
	public TaskWriteService(List<Task> tasks, TaskRepository repository, ConsoleView view) {
		this.tasks = tasks;
		this.repository = repository;
		this.view = view;
		
		
		int maxId = tasks.stream()
				.mapToInt(Task::getId)
				.max()
				.orElse(0);
		
		this.nextId.set(maxId);
	}	
	
	private int generateId() {
		int id = nextId.incrementAndGet();
		return id;
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
	
	public void deleteTask(int id) {
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
