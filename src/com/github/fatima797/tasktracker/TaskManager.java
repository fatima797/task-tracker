package com.github.fatima797.tasktracker;

import java.util.ArrayList;
import java.util.List;

public class TaskManager {
	private List<Task> tasks;
	private final String filepath;
	
	public TaskManager(String filepath) {
		this.tasks = new ArrayList<>();
		this.filepath =  filepath;
	}
	
	public void addTask(String description) {
		
	}
	
	public void updateTask(int id, String newDescription, Status newStatus) {
		
	}
	
	public void saveTasks() {
		
	}
	
	public void loadTasks() {
		
	}
	
	public void deleteTask(int id) {
		
	}

}
