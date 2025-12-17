package com.github.fatima797.tasktracker.repository;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import com.github.fatima797.tasktracker.model.Status;
import com.github.fatima797.tasktracker.model.Task;
import com.github.fatima797.tasktracker.view.ConsoleView;

/**
 * File-based implementation of {@link TaskRepository}.
 *
 * Persists tasks to a JSON file and loads them back into memory.
 */

public class FileTaskRepository implements TaskRepository{
	private static final String FILEPATH = "tasks/tasks.json";
	private List<Task> tasks = null;
	private final ConsoleView view;
	
	public FileTaskRepository(ConsoleView view) {
		this.view = view;
	}
	
	public void saveToFile() {
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

		try {
			// Convert the JSON content into bytes for file writing
			byte[] content = sb.toString().getBytes(StandardCharsets.UTF_8);
			
			// Make sure directory exists
			Files.createDirectories(path.getParent()); 

			// Write the byte content to the file (overwrites if already exists)
			Files.write(path, 
					content, 
					StandardOpenOption.CREATE, 
					StandardOpenOption.TRUNCATE_EXISTING);

		} catch (IOException e) {
			view.showError("Error: Failed to save tasks to file.");
			e.printStackTrace();
		}

	}
	
	/**
	 * Loads tasks from a JSON file into memory by manually parsing JSON.
	 * 
	 * This method avoids using external libraries by:
	 * - Reading raw file content as a string
	 * - Removing array brackets and splitting individual task JSON objects
	 * - Delegating each object to a parsing helper
	 * 
	 * @return a list of Task objects loaded from file
	 */
	
	private List<Task> loadTasksFromFile() {
		// Initialize list that will hold tasks loaded from file
        List<Task> loadedTasks = new ArrayList<>();
        Path path = Paths.get(FILEPATH);

        // If file does not exist, return empty list
        if (!Files.exists(path)) {
            return loadedTasks;
        }
        
        try {
        	// Read all lines from file into a single string
            List<String> lines = Files.readAllLines(path);
            StringBuilder sb = new StringBuilder();
            
            for (String line : lines) {
                sb.append(line.trim()).append("\n");
            }

            String jsonContent = sb.toString()
                .replace("[", "")
                .replace("]", "")
                .trim();

            if (jsonContent.isEmpty()) {
                return loadedTasks;
            }

         // Split string at into individual JSON object strings
            String[] objectStrings = jsonContent.split("},\\s*");

            for (int i = 0; i < objectStrings.length; i++) {
                String objStr = objectStrings[i].trim();
                if (i < objectStrings.length - 1) {
                    objStr += "}"; // Add closing bracket if split removed it
                }

                // Delegate the JSON key-value parsing to a separate helper method
                Task task = parseTaskFromJson(objStr);
                
                if (task != null) {
                    loadedTasks.add(task);
                }
            }

        } catch (IOException e) {
        	view.showError("Error: Failed to read tasks.json");
        	e.printStackTrace();
        }

        return loadedTasks;
    }

	
	/**
	 * Parses a single raw JSON object string and reconstructs a Task object.
	 *
	 * @param jsonObj the JSON string representing one task
	 * @return the reconstructed Task object, or null if parsing fails
	 */
	private Task parseTaskFromJson(String jsonObj) {
	
		try {
			// Remove enclosing brackets and whitespace
            String cleaned = jsonObj.replace("{", "").replace("}", "").trim();
            
            // Split into individual key-value lines
            String[] keyValueLines = cleaned.split(",\\s*");

            int id = 0;
            String description = "";
            String status = "";
            String createdAt = "";
            String updatedAt = "";

            for (String line : keyValueLines) {
                String[] parts = line.split(":", 2);
                if (parts.length < 2) continue;

                String key = parts[0].trim().replace("\"", "");
                String value = parts[1].trim().replace("\"", "");

                switch (key) {
                    case "id": id = Integer.parseInt(value); break;
                    case "description": description = value; break;
                    case "status": status = value; break;
                    case "createdAt": createdAt = value; break;
                    case "updatedAt": updatedAt = value; break;
                }
            }

            // Reconstruct Task
            return Task.restore(
                id,
                description,
                Status.valueOf(status.toUpperCase()),
                LocalDateTime.parse(createdAt),
                LocalDateTime.parse(updatedAt)
            );

        } catch (Exception e) {
        	view.showError("Error: Failed to parse a Task JSON object.");
            e.printStackTrace();
            return null;
        }
	}
	
	private void ensureTasksLoaded() {
		if(tasks == null) {
			tasks = loadTasksFromFile();
		}
	}

	@Override
	public List<Task> findAll() {
		ensureTasksLoaded();
		return new ArrayList<>(tasks);
	}

	@Override
	public Optional<Task> findById(int id) {
		ensureTasksLoaded();
		return tasks.stream()
				.filter(task -> task.getId() == id)
				.findFirst();
	}
	
	@Override
	public List<Task> findByStatus(Status status) {
		ensureTasksLoaded();
		List<Task> tasksWithMatchingStatus = new ArrayList<>();
		for(Task task : tasks) {
			if(task.getStatus() == status) {
				tasksWithMatchingStatus.add(task);
			}
		}
		return tasksWithMatchingStatus;
	}
	
	@Override
	public void add(Task task) {
		if(tasks == null) {
			ensureTasksLoaded();
		}
		tasks.add(task);
		saveToFile();	
	}

	@Override
	public boolean delete(int id) {
		if(tasks == null) {
			ensureTasksLoaded();
		}
		
		boolean removed = tasks.removeIf(task -> task.getId() == id);
		if(removed) {
			saveToFile();
		}
		return removed;		
	}

	@Override
	public boolean update(Task updatedTask) {
		if(tasks == null) {
			ensureTasksLoaded();
		}
		
		for (Task task: tasks) {
			if(task.getId() == updatedTask.getId()) {
				task.setDescription(updatedTask.getDescription());
				task.setStatus(updatedTask.getStatus());
				task.updateTimestamp();
				saveToFile();
				return true;
			}
		}
		return false;
	}	
}
