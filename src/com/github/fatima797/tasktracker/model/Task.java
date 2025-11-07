package com.github.fatima797.tasktracker.model;

import java.time.LocalDateTime;

public class Task {
	private int id;
	private String description;
	private Status status; // "todo", "in-progress", "done"
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;

	public Task () {
	}

	public Task(int id, String description) {
		this.id = id;
		this.description = description;
		this.status = Status.TODO; // every new task has 'todo' starting state
		this.createdAt = LocalDateTime.now();
		this.updatedAt = LocalDateTime.now();
	}


	// This constructor is for reconstructing tasks when loading from JSON
	public Task(int id, String description, Status status, LocalDateTime createdAt, LocalDateTime updatedAt) {
		this.id = id;
		this.description = description;
		this.status = status;
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
	}

	// Static factory method for reconstructing existing tasks
	public static Task restore(int id, String description, Status status, LocalDateTime createdAt, LocalDateTime updatedAt) {
		return new Task(id, description, status, createdAt, updatedAt);
	}


	public int getId() {
		return id;
	}

	public String getDescription() {
		return description;
	}


	public void setDescription(String description) {
		this.description = description;
	}


	public Status getStatus() {
		return status;
	}


	public void setStatus(Status status) {
		this.status = status;
	}


	public LocalDateTime getCreatedAt() {
		return createdAt;
	}


	public LocalDateTime getUpdatedAt() {
		return updatedAt;
	}


	// Helpers
	public void updateTimestamp() {
		this.updatedAt = LocalDateTime.now();
	}


	@Override
	public String toString() {
		return "[" + id + "] " + description + " " + status + " - createdAt: " + createdAt + " " + "updatedAt: " + updatedAt;
	}

	public String toJson() {

		StringBuilder sb = new StringBuilder();
		sb.append("{\n");
		sb.append("\"id\": ").append(id).append(",\n");
		sb.append("\"description\": \"").append(description).append("\",\n");
		sb.append("\"status\": \"").append(status).append("\",\n");
		sb.append("\"createdAt\": \"").append(createdAt).append("\",\n");
		sb.append("\"updatedAt\": \"").append(updatedAt).append("\"\n");
		sb.append("}");
		
		return sb.toString();
	}
	
	public String toDisplayString() {
		return "[" + id + "] " + description + " (" + status + ") - Created: " + createdAt + " - Updated: " + updatedAt;
	}

}
