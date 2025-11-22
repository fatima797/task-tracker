package com.github.fatima797.tasktracker.view;

import com.github.fatima797.tasktracker.model.Status;
import com.github.fatima797.tasktracker.model.Task;

public class ConsoleView {
	
	public void showTaskAdded(Task task) {
		System.out.println("Task added successfully (ID: " + task.getId() + ")");
	}
	
	public void showTaskDeleted(int id) {
		System.out.println("Task " + id + " deleted successfully");
	}
	
	public void showTaskUpdated(int id) {
		System.out.println("Task ID " + id + " updated successfully.");
	}
	
	public void showTaskListIsEmpty() {
		System.out.println("No tasks available.");
	}
	
	public void showStatusHeader(Status desiredStatus) {
		System.out.println("=== Tasks with status: " + desiredStatus.name() + " ===");	
	}
	
	public void showTask(Task task) {
		System.out.println(task.toDisplayString());
	}

	public void showTaskListHeader() {
		System.out.println("=== All Tasks ===");
	}
	
	
	public void showTaskStatusUpdated(int id, Status newStatus) {
		System.out.println("Task " + id + " marked as " + newStatus);
	}
	
	public void showError(String message) {
		System.out.println("Error: " + message);
	}

}
