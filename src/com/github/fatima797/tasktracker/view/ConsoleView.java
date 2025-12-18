package com.github.fatima797.tasktracker.view;

import com.github.fatima797.tasktracker.model.Status;
import com.github.fatima797.tasktracker.model.Task;

/**
 * Responsible solely for displaying output to the console.
 * 
 * <p>All business logic is performed in service classes, while this class 
 * contains only formatting and output concerns</p> 
 */
public class ConsoleView {

	public void showWelcome() {
		System.out.println("Welcome to the Task Tracker CLI App!");
		System.out.println();
		System.out.println("With Task Tracker, you can:");
		System.out.println("  - Add, update, or delete tasks");
		System.out.println("  - List all tasks or filter by status");
		System.out.println("  - Mark tasks as in progress or done");
		System.out.println();
		System.out.println("Task statuses include: TODO, IN_PROGRESS, and DONE");
		System.out.println();
		System.out.println("Usage examples:");
		System.out.println("  java -jar task-tracker.jar add \"My new task\"");
		System.out.println("  java -jar task-tracker.jar update <ID>");
		System.out.println("  java -jar task-tracker.jar delete <ID>");
		System.out.println("  java -jar task-tracker.jar list");
		System.out.println("  java -jar task-tracker.jar list TODO");
		System.out.println("  java -jar task-tracker.jar mark-done <ID>");
		System.out.println("  java -jar task-tracker.jar mark-in-progress <ID>");
		System.out.println("  java -jar task-tracker.jar help");
		System.out.println();
	}

	public void showHelp() {
		System.out.println("Task Tracker CLI - Help");
		System.out.println();
		System.out.println("Available commands:");
		System.out.println("  add <description>       Add a new task");
		System.out.println("  update <id>             Update task description");
		System.out.println("  delete <id>             Delete a task");
		System.out.println("  list                    List all tasks");
		System.out.println("  list <status>           List tasks by status (TODO, IN_PROGRESS, DONE)");
		System.out.println("  mark-done <id>          Mark a task as done");
		System.out.println("  mark-in-progress <id>   Mark a task as in progress");
		System.out.println();
		System.out.println("Usage:");
		System.out.println("  java -jar task-tracker.jar <command> [arguments]");
		System.out.println();
		System.out.println("Tip: Run the app without arguments to see full usage instructions.");
	}
	
	public void showTaskAdded(Task task) {
		System.out.println("Task added successfully (ID: " + task.getId() + ")");
	}

	public void showTaskDeleted(int id) {
		System.out.println("Task " + id + " deleted successfully");
	}

	public void showTaskUpdated(int id) {
		System.out.println("Task ID " + id + " updated successfully.");
	}

	public void showTasksIsEmpty() {
		System.out.println("No tasks available.");
	}

	public void showStatusHeader(Status desiredStatus) {
		System.out.println("=== Tasks with status: " + desiredStatus.name() + " ===");	
	}

	public void showTask(Task task) {
		System.out.println(task.toDisplayString());
	}

	public void showHeadingForAllTasks() {
		System.out.println("=== All Tasks ===");
	}


	public void showTaskStatusUpdated(int id, Status newStatus) {
		System.out.println("Task " + id + " marked as " + newStatus);
	}

	public void showError(String message) {
		System.out.println("Error: " + message);
	}

}
