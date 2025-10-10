package com.github.fatima797.tasktracker;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class TaskManager {
	private static final String FILEPATH = "tasks/tasks.json";
	private List<Task> tasks;
	private static AtomicInteger nextId = new AtomicInteger(0);

	
	public TaskManager() {
		this.tasks = new ArrayList<>();
	}
	
	public void addTask(String description) {
		int newId = generateId();
		Task newTask = new Task(newId, description);
		tasks.add(newTask);
		saveTasks();
		
		System.out.println("Task added successfully (ID: " + newTask.getId() + ")");
	}
	
	
	public void saveTasks() {
		Path path = Paths.get(FILEPATH);
		
		StringBuilder sb = new StringBuilder();
		
		sb.append("[\n");
		for(int i=0; i < tasks.size(); i++) {
			Task task = tasks.get(i);
			sb.append(task.toJson());
			
			if(i != tasks.size()-1) {
				sb.append(",\n");
			}else {
				sb.append("\n");
			}
		}
		sb.append("]");
		
		
		try {
			Files.write(path, sb.toString().getBytes(StandardCharsets.UTF_8), StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.WRITE);
		} catch (IOException e) {
			e.printStackTrace();
		}

		
	}
	
	private int generateId() {
		return nextId.incrementAndGet();
	}

}
