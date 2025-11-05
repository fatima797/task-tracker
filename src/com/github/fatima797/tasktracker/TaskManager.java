package com.github.fatima797.tasktracker;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class TaskManager {
	private static final String FILEPATH = "tasks/tasks.json";
	private List<Task> tasks = new ArrayList<>();
	private AtomicInteger nextId = new AtomicInteger(0);

	public TaskManager() {
		loadTasks();
	}

	public void addTask(String description) {

		// Generate unique ID based on highest existing task ID
		int newId = generateId();
		Task newTask = new Task(newId, description);
		// Add new task to in-memory list
		tasks.add(newTask);

		// Save task list to JSON file
		saveTasks();

		System.out.println("Task added successfully (ID: " + newTask.getId() + ")");
	}


	public void saveTasks() {
		Path path = Paths.get(FILEPATH);

		// Use StringBuilder to construct JSON string
		StringBuilder sb = new StringBuilder();

		sb.append("[\n"); // Open the JSON array

		// Loop through all tasks and convert each to JSON string
		for (int i = 0; i < tasks.size(); i++) {
			Task task = tasks.get(i);
			sb.append(task.toJson());

			// Append comma for all except the last task
			if (i != tasks.size() - 1) {
				sb.append(",\n");
			} else {
				sb.append("\n");
			}
		}
		sb.append("]"); // Close the JSON array

		// Convert the JSON content into bytes for file writing
		byte[] content = sb.toString().getBytes(StandardCharsets.UTF_8);

		try {
			// Make sure directory exists
			Files.createDirectories(path.getParent()); 

			// Write the byte content to the file (overwrites if already exists)
			Files.write(path, content, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);

		} catch (IOException e) {
			System.out.println("Failed to save tasks to file.");
			e.printStackTrace();
		}


	}

	private int generateId() {
		int id = nextId.incrementAndGet();
		return id;
	}

	public void loadTasks() {
		tasks.clear();

		// Define file path where tasks are stored
		Path path = Paths.get(FILEPATH);


		if (!Files.exists(path)) {
			return;
		}

		try {
			// Read all lines from JSON file into list of strings
			List<String> lines = Files.readAllLines(path);

			// If file is empty, no tasks to load
			if(lines.isEmpty()) {
				return;
			}

			// Accumulate all lines into StringBuilder for parsing
			StringBuilder sb = new StringBuilder();
			for(String line : lines) {
				sb.append(line.trim()).append("\n");

			}

			// Convert combined text into a single JSON-like string
			// and remove surrounding square brackets from JSON array
			String jsonContent = sb.toString()
					.replace("[", "")
					.replace("]", "")
					.trim();

			// Split into individual object strings at "},"
			String[] objectStrings = jsonContent.split("},\\s*");

			// Keep track of highest ID (used later for ID generation)
			int highestId = 0; 

			// Parse each object string and convert into a Task instance
			for(int i = 0; i < objectStrings.length; i++) {

				String objStr = objectStrings[i].trim();

				// Add closing } if it was removed during splitting
				if(i < objectStrings.length - 1) {
					objStr += "}";
				}

				try {
					// Remove braces and split into key-value pairs
					String object = objStr.replace("{", "").replace("}", "").trim();
					String[] keyValueLines = object.split(",\\s*");

					int id = 0;
					String description = "";
					String status = "";
					String createdAt = "";
					String updatedAt = "";

					// Parse each key-value pair from the JSON object
					for (String line : keyValueLines) {
						String[] parts = line.split(":", 2); // Split at colon, limit to 2 parts

						if (parts.length < 2) continue;

						// Remove quotations
						String key = parts[0].trim().replace("\"", "");
						String value = parts[1].trim().replace("\"", "");

						// Map JSON keys to Task properties
						switch (key) {
						case "id":
							id = Integer.parseInt(value);
							if (id > highestId) {
								highestId = id;
							}
							break;
						case "description":
							description = value;
							break;
						case "status":
							status = value;
							break;
						case "createdAt":
							createdAt = value;
							break;
						case "updatedAt":
							updatedAt = value;
							break;
						default:
							System.out.println("Unrecognized key: " + key);
						}
					}

					// Restore a Task object using parsed values from JSON file
					Task task = Task.restore(
							id, 
							description, 
							Status.valueOf(status.toUpperCase()), 
							LocalDateTime.parse(createdAt), 
							LocalDateTime.parse(updatedAt));

					// Add constructed Task to in-memory task list
					tasks.add(task);

				} catch (Exception e) {
					System.out.println("Failed to parse a task. Skipping it.");
					e.printStackTrace();
				}
			}

			// Set highestId to be used to generate next ID in generateId()
			nextId.set(highestId);

		} catch (IOException e) {
			System.out.println("Failed to read tasks.json");
			e.printStackTrace();
		}
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
			saveTasks();
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
			saveTasks();
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
			saveTasks();
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
