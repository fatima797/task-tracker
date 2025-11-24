package com.github.fatima797.tasktracker.service;

import java.util.Arrays;
import java.util.List;

import com.github.fatima797.tasktracker.model.Status;
import com.github.fatima797.tasktracker.model.Task;
import com.github.fatima797.tasktracker.view.ConsoleView;

public class TaskQueryService {
	private final List<Task> tasks;
	private final ConsoleView view;

	public TaskQueryService(List<Task> tasks, ConsoleView view) {
		this.tasks = tasks;
		this.view = view;
	}

	public void listAll() {
		if(tasks.isEmpty()) {
			view.showTaskListIsEmpty();
			return;
		}

		view.showTaskListHeader();
		for(Task task : tasks) {
			view.showTask(task);
		}
	}

	public void listByStatus(String statusInput) {
		Status desiredStatus;
		boolean found = false;

		try {
			// Convert to enum and store the result
			desiredStatus = Status.valueOf(statusInput.toUpperCase());
		} catch (IllegalArgumentException e) {
			view.showError("Error: Invalid status. Valid options are: " + Arrays.toString(Status.values()));
			return; 
		}

		if(tasks.isEmpty()) {
			view.showTaskListIsEmpty();
			return;
		}

		view.showStatusHeader(desiredStatus);
		
		for(Task task : tasks) {
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

}
