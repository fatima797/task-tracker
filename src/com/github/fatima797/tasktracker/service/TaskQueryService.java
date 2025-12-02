package com.github.fatima797.tasktracker.service;

import java.util.List;

import com.github.fatima797.tasktracker.model.Status;
import com.github.fatima797.tasktracker.model.Task;
import com.github.fatima797.tasktracker.repository.TaskRepository;
import com.github.fatima797.tasktracker.view.ConsoleView;

/**
 * Handles all read operations for retrieving and displaying tasks (listAll, listByStatus).
 * 
 * <p>This service is the 'query' side of the CQS (Command Query Separation) principle,
 * ensuring all query operations occur in this class.</p>
 * 
 * {@link ConsoleView} is injected to perform output.
 */

public class TaskQueryService {
	private final TaskRepository repository;
	private final ConsoleView view;

	public TaskQueryService(TaskRepository repository, ConsoleView view) {
		this.repository = repository;
		this.view = view;
	}

	public void listAll() {
		List<Task> tasks = repository.findAll();

		if (tasks.isEmpty()) {
			view.showTasksIsEmpty();
			return;
		}

		view.showHeadingForAllTasks();
		for (Task task : tasks) {
			view.showTask(task);
		}
	}


	public void listByStatus(String statusInput) {
		Status desiredStatus;

		try {
			desiredStatus = Status.valueOf(statusInput.toUpperCase());
		} catch (IllegalArgumentException e) {
			view.showError("Invalid status. Valid options: TODO, IN_PROGRESS, DONE");
			return;
		}

		List<Task> filtered = repository.findByStatus(desiredStatus);

		if (filtered.isEmpty()) {
			view.showError("No tasks found with status: " + desiredStatus.name());
			return;
		}

		view.showStatusHeader(desiredStatus);
		for (Task task : filtered) {
			view.showTask(task);
		}
	}

}
