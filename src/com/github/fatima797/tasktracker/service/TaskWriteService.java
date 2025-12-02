package com.github.fatima797.tasktracker.service;

import java.util.List;
import com.github.fatima797.tasktracker.model.Status;
import com.github.fatima797.tasktracker.model.Task;
import com.github.fatima797.tasktracker.repository.TaskRepository;
import com.github.fatima797.tasktracker.view.ConsoleView;

/**
 * Handles all commands that modify task state (create, update, delete, 
 * and status changes). 
 *
 * <p>This service is the 'write' side of the CQS (Command Query Separation) 
 * principle, ensuring all mutations occur in one place and are consistently 
 * persisted through {@link TaskRepository}.</p>
 *
 * {@link ConsoleView} is injected to handle presentation.
 */

public class TaskWriteService {
	private final TaskRepository repository;
	private final ConsoleView view;


	public TaskWriteService(TaskRepository repository, ConsoleView view) {
		this.repository = repository;
		this.view = view;
	}	

	private int generateNextId() {
		return repository.findAll()
				.stream()
				.mapToInt(Task::getId)
				.max()
				.orElse(0) + 1;
	}

	public void addTask(String description) {
		// Generate unique ID based on highest existing task ID
		int newId = generateNextId();
		Task newTask = new Task(newId, description);
		repository.add(newTask);
		view.showTaskAdded(newTask);
	}

	public void deleteTask(int id) {
		boolean removed = repository.delete(id);

		if (removed) {
			view.showTaskDeleted(id);
		} else {
			view.showError("Task " + id + " not found.");

		}
	}
	
	public void updateTask(int id, String newDescription) {
		Task task = findTaskOrShowError(id);
		
		if(task == null) return;
			
		task.setDescription(newDescription);
		task.updateTimestamp();
		repository.update(task);
		view.showTaskUpdated(id);
		
	}

	public void updateTaskStatus(int id, Status newStatus) {
		Task task = findTaskOrShowError(id);
		
		if(task == null) return;
		
		task.setStatus(newStatus);
		task.updateTimestamp(); 
		repository.update(task);
		view.showTaskStatusUpdated(id, newStatus);    

	}
	
	private Task findTaskOrShowError(int id) {
		List<Task> all = repository.findAll();
		
		for(Task task : all) {
			if(task.getId() == id) {
				return task;
			}
		}
		view.showError("Task " + id + " not found.");
		
		return null;
	}
	

	public void markTaskAsDone(int id) {
		updateTaskStatus(id, Status.DONE);
	}

	public void markInProgress(int id) {
		updateTaskStatus(id, Status.IN_PROGRESS);
	}

}
